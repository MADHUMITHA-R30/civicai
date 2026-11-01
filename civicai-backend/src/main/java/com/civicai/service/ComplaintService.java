package com.civicai.service;

import com.civicai.dto.ComplaintDTOs.*;
import com.civicai.dto.ResolutionDTOs.*;
import com.civicai.entity.*;
import com.civicai.enums.*;
import com.civicai.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ComplaintService {

    @Autowired private ComplaintRepository complaintRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private IssueCategoryRepository categoryRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private ComplaintMediaRepository mediaRepository;
    @Autowired private AiAnalysisRepository aiAnalysisRepository;
    @Autowired private ComplaintClusterRepository clusterRepository;
    @Autowired private ClusterMemberRepository clusterMemberRepository;
    @Autowired private ResolutionRepository resolutionRepository;
    @Autowired private ResolutionMediaRepository resolutionMediaRepository;
    @Autowired private ComplaintStatusHistoryRepository historyRepository;
    @Autowired private NotificationRepository notificationRepository;
    @Autowired private AuditLogRepository auditLogRepository;
    
    @Autowired private FileStorageService fileStorageService;
    @Autowired private AIServiceClient aiServiceClient;

    @Transactional
    public ComplaintResponseDTO createComplaint(Long citizenId, CreateComplaintDTO dto) {
        User citizen = userRepository.findById(citizenId)
                .orElseThrow(() -> new RuntimeException("Citizen not found"));

        if ("BLOCKED".equalsIgnoreCase(citizen.getAccountStatus())) {
            throw new RuntimeException("Your account is blocked. Cannot submit complaints.");
        }

        IssueCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Invalid Category"));

        Complaint complaint = new Complaint();
        complaint.setCitizen(citizen);
        complaint.setCategory(category);
        complaint.setDescription(dto.getDescription());
        complaint.setAddress(dto.getAddress());
        complaint.setLatitude(dto.getLatitude());
        complaint.setLongitude(dto.getLongitude());
        complaint.setContactNumber(dto.getContactNumber() != null ? dto.getContactNumber() : citizen.getMobile());
        complaint.setIsVoiceReport(dto.getIsVoiceReport() != null && dto.getIsVoiceReport());
        complaint.setStatus(ComplaintStatus.SUBMITTED);

        // Assign default department based on category
        Department department = category.getDefaultDepartment();
        complaint.setDepartment(department);

        // Unique Complaint Number CIV-2026-XXXXX
        String code = "CIV-2026-" + String.format("%05d", (System.currentTimeMillis() % 100000));
        complaint.setComplaintNumber(code);

        Complaint saved = complaintRepository.save(complaint);

        // Save original photo if uploaded
        if (dto.getPhotoBase64() != null && !dto.getPhotoBase64().isEmpty()) {
            String path = fileStorageService.saveBase64Image(dto.getPhotoBase64(), "complaint_" + saved.getId());
            if (path != null) {
                ComplaintMedia media = new ComplaintMedia(saved, "IMAGE", path);
                mediaRepository.save(media);
            }
        }

        // Run AI Analysis & Auto Prioritization
        runAiPipeline(saved);

        // Record Initial History
        historyRepository.save(new ComplaintStatusHistory(saved, null, ComplaintStatus.SUBMITTED, citizen, "Complaint submitted by citizen"));

        // Notify Citizen
        notificationRepository.save(new Notification(citizen, saved, "Complaint Registered", "Your complaint " + code + " has been successfully submitted and assigned to " + (department != null ? department.getName() : "Department")));

        return mapToDTO(saved);
    }

    private void runAiPipeline(Complaint complaint) {
        try {
            long totalCitizenReports = complaintRepository.countByCitizenId(complaint.getCitizen().getId());
            Map<String, Object> aiResult = aiServiceClient.analyzeComplaint(complaint.getDescription(), (int) totalCitizenReports);

            AiAnalysis analysis = new AiAnalysis();
            analysis.setComplaint(complaint);
            analysis.setExtractedCategory((String) aiResult.get("issue_category"));
            analysis.setExtractedAddress((String) aiResult.get("address"));
            analysis.setExtractedContact((String) aiResult.get("contact_number"));
            analysis.setSeverity((String) aiResult.get("severity"));
            
            Number prio = (Number) aiResult.get("priority_score");
            int prioScore = prio != null ? prio.intValue() : 50;
            analysis.setPriorityScore(prioScore);
            complaint.setPriorityScore(prioScore);

            if (prioScore >= 80) complaint.setPriority(PriorityLevel.CRITICAL);
            else if (prioScore >= 65) complaint.setPriority(PriorityLevel.HIGH);
            else if (prioScore >= 45) complaint.setPriority(PriorityLevel.MEDIUM);
            else complaint.setPriority(PriorityLevel.LOW);

            analysis.setDepartmentPrediction((String) aiResult.get("department"));
            Number conf = (Number) aiResult.get("confidence");
            analysis.setConfidence(conf != null ? conf.doubleValue() : 0.85);
            complaint.setAiConfidence(analysis.getConfidence());

            List<String> reasons = (List<String>) aiResult.get("reasons");
            if (reasons != null && !reasons.isEmpty()) {
                analysis.setPriorityExplanation(String.join("; ", reasons));
            } else {
                analysis.setPriorityExplanation("Auto-calculated based on safety and public impact factor.");
            }

            aiAnalysisRepository.save(analysis);
            complaint.setStatus(ComplaintStatus.AI_ANALYZED);
            complaintRepository.save(complaint);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> processVoiceInput(String audioBase64) {
        Map<String, Object> transcribeRes = aiServiceClient.transcribeVoice(audioBase64);
        String text = (String) transcribeRes.get("text");

        Map<String, Object> analysisRes = aiServiceClient.analyzeComplaint(text, 1);

        Map<String, Object> result = new HashMap<>();
        result.put("transcribedText", text);
        result.put("extracted", analysisRes);
        return result;
    }

    public Map<String, Object> checkDuplicate(Long categoryId, String description, String address, Double lat, Double lon) {
        List<Complaint> activeComplaints = complaintRepository.findAll();
        List<Map<String, Object>> complaintList = new ArrayList<>();
        
        for (Complaint c : activeComplaints) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", c.getId());
            m.put("complaintNumber", c.getComplaintNumber());
            m.put("categoryId", c.getCategory().getId());
            m.put("categoryName", c.getCategory().getName());
            m.put("description", c.getDescription());
            m.put("address", c.getAddress());
            m.put("status", c.getStatus().name());
            m.put("latitude", c.getLatitude());
            m.put("longitude", c.getLongitude());
            complaintList.add(m);
        }

        return aiServiceClient.detectDuplicate(categoryId, description, address, lat, lon, complaintList);
    }

    public List<ComplaintResponseDTO> getMyComplaints(Long citizenId) {
        return complaintRepository.findByCitizenIdOrderByCreatedAtDesc(citizenId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ComplaintResponseDTO> getDepartmentComplaints(Long deptId, ComplaintStatus status, PriorityLevel priority) {
        List<Complaint> list;
        if (deptId == null) {
            list = complaintRepository.findAllFiltered(null, status);
        } else {
            list = complaintRepository.findByDepartmentFiltered(deptId, status, priority);
        }
        return list.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public ComplaintResponseDTO getComplaintById(Long complaintId) {
        Complaint c = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        return mapToDTO(c);
    }

    @Transactional
    public ComplaintResponseDTO updateStatus(Long complaintId, Long userId, StatusUpdateDTO dto) {
        Complaint c = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ComplaintStatus oldStatus = c.getStatus();
        ComplaintStatus newStatus = ComplaintStatus.valueOf(dto.getStatus());
        c.setStatus(newStatus);
        complaintRepository.save(c);

        historyRepository.save(new ComplaintStatusHistory(c, oldStatus, newStatus, user, dto.getRemarks()));
        notificationRepository.save(new Notification(c.getCitizen(), c, "Status Updated: " + newStatus.name(), "Your complaint " + c.getComplaintNumber() + " status is now " + newStatus.name() + ". Remarks: " + (dto.getRemarks() != null ? dto.getRemarks() : "None")));

        return mapToDTO(c);
    }

    @Transactional
    public ComplaintResponseDTO resolveComplaint(Long complaintId, Long adminId, ResolveComplaintDTO dto) {
        Complaint c = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        if (dto.getAfterPhotoBase64() == null || dto.getAfterPhotoBase64().trim().isEmpty()) {
            throw new RuntimeException("After-resolution photograph proof is mandatory!");
        }

        ComplaintStatus oldStatus = c.getStatus();
        c.setStatus(ComplaintStatus.RESOLVED);
        complaintRepository.save(c);

        Resolution resolution = new Resolution(c, dto.getResolutionMessage(), admin);
        resolution.setCitizenVerificationStatus("PENDING");
        Resolution savedRes = resolutionRepository.save(resolution);

        String afterPath = fileStorageService.saveBase64Image(dto.getAfterPhotoBase64(), "resolution_" + savedRes.getId());
        if (afterPath != null) {
            resolutionMediaRepository.save(new ResolutionMedia(savedRes, afterPath));
        }

        historyRepository.save(new ComplaintStatusHistory(c, oldStatus, ComplaintStatus.RESOLVED, admin, "Resolved with after-resolution photo evidence."));
        notificationRepository.save(new Notification(c.getCitizen(), c, "Issue Resolved! Please Verify", "Your complaint " + c.getComplaintNumber() + " has been marked resolved by " + admin.getDepartment().getName() + ". Please inspect the before/after proof and verify resolution."));

        return mapToDTO(c);
    }

    @Transactional
    public ComplaintResponseDTO verifyResolution(Long complaintId, Long citizenId, VerificationRequestDTO dto) {
        Complaint c = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        
        Resolution res = resolutionRepository.findByComplaintId(complaintId)
                .orElseThrow(() -> new RuntimeException("Resolution record not found"));

        res.setCitizenFeedback(dto.getFeedback());

        if (Boolean.TRUE.equals(dto.getIsResolved())) {
            res.setCitizenVerificationStatus("VERIFIED");
            c.setStatus(ComplaintStatus.VERIFIED);
            historyRepository.save(new ComplaintStatusHistory(c, ComplaintStatus.RESOLVED, ComplaintStatus.VERIFIED, c.getCitizen(), "Citizen verified resolution: " + dto.getFeedback()));
        } else {
            res.setCitizenVerificationStatus("DISPUTED");
            c.setStatus(ComplaintStatus.RESOLUTION_DISPUTED);
            historyRepository.save(new ComplaintStatusHistory(c, ComplaintStatus.RESOLVED, ComplaintStatus.RESOLUTION_DISPUTED, c.getCitizen(), "Citizen disputed resolution: " + dto.getFeedback()));
            
            // Notify Department Admin & Super Admin of Dispute
            if (c.getDepartment() != null) {
                List<User> deptAdmins = userRepository.findByDepartmentId(c.getDepartment().getId());
                for (User admin : deptAdmins) {
                    notificationRepository.save(new Notification(admin, c, "⚠️ Resolution Disputed!", "Citizen disputed resolution for issue " + c.getComplaintNumber() + ". Feedback: " + dto.getFeedback()));
                }
            }
        }

        resolutionRepository.save(res);
        complaintRepository.save(c);

        return mapToDTO(c);
    }

    public ComplaintResponseDTO mapToDTO(Complaint c) {
        ComplaintResponseDTO dto = new ComplaintResponseDTO();
        dto.setId(c.getId());
        dto.setComplaintNumber(c.getComplaintNumber());
        dto.setCitizenId(c.getCitizen().getId());
        dto.setCitizenName(c.getCitizen().getName());
        dto.setCitizenMobile(c.getCitizen().getMobile());
        dto.setCategoryId(c.getCategory().getId());
        dto.setCategoryName(c.getCategory().getName());
        dto.setDepartmentId(c.getDepartment() != null ? c.getDepartment().getId() : null);
        dto.setDepartmentName(c.getDepartment() != null ? c.getDepartment().getName() : "Unassigned");
        dto.setDescription(c.getDescription());
        dto.setAddress(c.getAddress());
        dto.setLatitude(c.getLatitude());
        dto.setLongitude(c.getLongitude());
        dto.setContactNumber(c.getContactNumber());
        dto.setPriority(c.getPriority().name());
        dto.setPriorityScore(c.getPriorityScore());
        dto.setStatus(c.getStatus().name());
        dto.setAiConfidence(c.getAiConfidence());
        dto.setIsVoiceReport(c.getIsVoiceReport());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setUpdatedAt(c.getUpdatedAt());

        // Media
        List<ComplaintMedia> mediaList = mediaRepository.findByComplaintId(c.getId());
        dto.setOriginalMediaPaths(mediaList.stream().map(ComplaintMedia::getFilePath).collect(Collectors.toList()));

        // Resolution
        resolutionRepository.findByComplaintId(c.getId()).ifPresent(res -> {
            ResolutionResponseDTO resDTO = new ResolutionResponseDTO();
            resDTO.setId(res.getId());
            resDTO.setResolutionMessage(res.getResolutionMessage());
            resDTO.setResolvedByName(res.getResolvedBy().getName());
            resDTO.setResolvedByDepartment(res.getResolvedBy().getDepartment() != null ? res.getResolvedBy().getDepartment().getName() : "Department");
            resDTO.setResolvedAt(res.getResolvedAt());
            resDTO.setCitizenVerificationStatus(res.getCitizenVerificationStatus());
            resDTO.setCitizenFeedback(res.getCitizenFeedback());

            List<ResolutionMedia> resMedia = resolutionMediaRepository.findByResolutionId(res.getId());
            resDTO.setAfterMediaPaths(resMedia.stream().map(ResolutionMedia::getFilePath).collect(Collectors.toList()));
            dto.setResolution(resDTO);
        });

        // AI Analysis
        aiAnalysisRepository.findByComplaintId(c.getId()).ifPresent(ai -> {
            AiAnalysisDTO aiDTO = new AiAnalysisDTO();
            aiDTO.setExtractedCategory(ai.getExtractedCategory());
            aiDTO.setExtractedAddress(ai.getExtractedAddress());
            aiDTO.setExtractedContact(ai.getExtractedContact());
            aiDTO.setSeverity(ai.getSeverity());
            aiDTO.setPriorityScore(ai.getPriorityScore());
            aiDTO.setDepartmentPrediction(ai.getDepartmentPrediction());
            aiDTO.setConfidence(ai.getConfidence());
            aiDTO.setDuplicateProbability(ai.getDuplicateProbability());
            aiDTO.setPriorityExplanation(ai.getPriorityExplanation());
            dto.setAiAnalysis(aiDTO);
        });

        // Cluster info
        clusterMemberRepository.findByComplaintId(c.getId()).ifPresent(cm -> {
            dto.setClusterReference(cm.getCluster().getClusterReference());
            dto.setClusterReportCount(cm.getCluster().getReportCount());
        });

        return dto;
    }
}
