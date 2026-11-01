package com.civicai.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ComplaintDTOs {

    public static class CreateComplaintDTO {
        private Long categoryId;
        private String description;
        private String address;
        private Double latitude;
        private Double longitude;
        private String contactNumber;
        private Boolean isVoiceReport = false;
        private String photoBase64; // Option for inline photo upload

        public Long getCategoryId() { return categoryId; }
        public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }

        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }

        public String getContactNumber() { return contactNumber; }
        public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

        public Boolean getIsVoiceReport() { return isVoiceReport; }
        public void setIsVoiceReport(Boolean isVoiceReport) { this.isVoiceReport = isVoiceReport; }

        public String getPhotoBase64() { return photoBase64; }
        public void setPhotoBase64(String photoBase64) { this.photoBase64 = photoBase64; }
    }

    public static class VoiceProcessRequest {
        private String audioBase64;

        public String getAudioBase64() { return audioBase64; }
        public void setAudioBase64(String audioBase64) { this.audioBase64 = audioBase64; }
    }

    public static class ComplaintResponseDTO {
        private Long id;
        private String complaintNumber;
        private Long citizenId;
        private String citizenName;
        private String citizenMobile;
        private Long categoryId;
        private String categoryName;
        private Long departmentId;
        private String departmentName;
        private String description;
        private String address;
        private Double latitude;
        private Double longitude;
        private String contactNumber;
        private String priority;
        private Integer priorityScore;
        private String status;
        private Double aiConfidence;
        private Boolean isVoiceReport;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private List<String> originalMediaPaths;
        private ResolutionDTOs.ResolutionResponseDTO resolution;
        private AiAnalysisDTO aiAnalysis;
        private String clusterReference;
        private Integer clusterReportCount;

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getComplaintNumber() { return complaintNumber; }
        public void setComplaintNumber(String complaintNumber) { this.complaintNumber = complaintNumber; }

        public Long getCitizenId() { return citizenId; }
        public void setCitizenId(Long citizenId) { this.citizenId = citizenId; }

        public String getCitizenName() { return citizenName; }
        public void setCitizenName(String citizenName) { this.citizenName = citizenName; }

        public String getCitizenMobile() { return citizenMobile; }
        public void setCitizenMobile(String citizenMobile) { this.citizenMobile = citizenMobile; }

        public Long getCategoryId() { return categoryId; }
        public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

        public String getCategoryName() { return categoryName; }
        public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

        public Long getDepartmentId() { return departmentId; }
        public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

        public String getDepartmentName() { return departmentName; }
        public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }

        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }

        public String getContactNumber() { return contactNumber; }
        public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }

        public Integer getPriorityScore() { return priorityScore; }
        public void setPriorityScore(Integer priorityScore) { this.priorityScore = priorityScore; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public Double getAiConfidence() { return aiConfidence; }
        public void setAiConfidence(Double aiConfidence) { this.aiConfidence = aiConfidence; }

        public Boolean getIsVoiceReport() { return isVoiceReport; }
        public void setIsVoiceReport(Boolean isVoiceReport) { this.isVoiceReport = isVoiceReport; }

        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

        public List<String> getOriginalMediaPaths() { return originalMediaPaths; }
        public void setOriginalMediaPaths(List<String> originalMediaPaths) { this.originalMediaPaths = originalMediaPaths; }

        public ResolutionDTOs.ResolutionResponseDTO getResolution() { return resolution; }
        public void setResolution(ResolutionDTOs.ResolutionResponseDTO resolution) { this.resolution = resolution; }

        public AiAnalysisDTO getAiAnalysis() { return aiAnalysis; }
        public void setAiAnalysis(AiAnalysisDTO aiAnalysis) { this.aiAnalysis = aiAnalysis; }

        public String getClusterReference() { return clusterReference; }
        public void setClusterReference(String clusterReference) { this.clusterReference = clusterReference; }

        public Integer getClusterReportCount() { return clusterReportCount; }
        public void setClusterReportCount(Integer clusterReportCount) { this.clusterReportCount = clusterReportCount; }
    }

    public static class AiAnalysisDTO {
        private String extractedCategory;
        private String extractedAddress;
        private String extractedContact;
        private String severity;
        private Integer priorityScore;
        private String departmentPrediction;
        private Double confidence;
        private Double duplicateProbability;
        private String priorityExplanation;

        public String getExtractedCategory() { return extractedCategory; }
        public void setExtractedCategory(String extractedCategory) { this.extractedCategory = extractedCategory; }

        public String getExtractedAddress() { return extractedAddress; }
        public void setExtractedAddress(String extractedAddress) { this.extractedAddress = extractedAddress; }

        public String getExtractedContact() { return extractedContact; }
        public void setExtractedContact(String extractedContact) { this.extractedContact = extractedContact; }

        public String getSeverity() { return severity; }
        public void setSeverity(String severity) { this.severity = severity; }

        public Integer getPriorityScore() { return priorityScore; }
        public void setPriorityScore(Integer priorityScore) { this.priorityScore = priorityScore; }

        public String getDepartmentPrediction() { return departmentPrediction; }
        public void setDepartmentPrediction(String departmentPrediction) { this.departmentPrediction = departmentPrediction; }

        public Double getConfidence() { return confidence; }
        public void setConfidence(Double confidence) { this.confidence = confidence; }

        public Double getDuplicateProbability() { return duplicateProbability; }
        public void setDuplicateProbability(Double duplicateProbability) { this.duplicateProbability = duplicateProbability; }

        public String getPriorityExplanation() { return priorityExplanation; }
        public void setPriorityExplanation(String priorityExplanation) { this.priorityExplanation = priorityExplanation; }
    }

    public static class StatusUpdateDTO {
        private String status;
        private String remarks;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getRemarks() { return remarks; }
        public void setRemarks(String remarks) { this.remarks = remarks; }
    }

    public static class FlagRequestDTO {
        private String flagType; // SPAM, FALSE_PHOTO, LOCATION_MISMATCH
        private String reason;

        public String getFlagType() { return flagType; }
        public void setFlagType(String flagType) { this.flagType = flagType; }

        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }

    public static class BlockUserRequestDTO {
        private String reason;
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
}
