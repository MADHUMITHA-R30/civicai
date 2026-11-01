package com.civicai.service;

import com.civicai.dto.ComplaintDTOs.*;
import com.civicai.entity.*;
import com.civicai.enums.ComplaintStatus;
import com.civicai.enums.FlagType;
import com.civicai.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AdminService {

    @Autowired private UserRepository userRepository;
    @Autowired private ComplaintRepository complaintRepository;
    @Autowired private UserFlagRepository userFlagRepository;
    @Autowired private BlockedUserRepository blockedUserRepository;
    @Autowired private AuditLogRepository auditLogRepository;
    @Autowired private AIServiceClient aiServiceClient;

    @Transactional
    public void flagComplaint(Long complaintId, Long adminId, FlagRequestDTO dto) {
        Complaint c = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        FlagType flagType = FlagType.valueOf(dto.getFlagType());
        UserFlag flag = new UserFlag(c.getCitizen(), c, flagType, dto.getReason(), admin);
        userFlagRepository.save(flag);

        if (flagType == FlagType.SPAM || flagType == FlagType.FALSE_PHOTO) {
            c.setStatus(ComplaintStatus.FALSE_SPAM);
            complaintRepository.save(c);
        }

        auditLogRepository.save(new AuditLog(admin, "FLAG_COMPLAINT", "Complaint", c.getId(), "Flagged complaint as " + flagType.name() + ". Reason: " + dto.getReason()));
    }

    public Map<String, Object> getUserReliability(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int totalSubmitted = (int) complaintRepository.countByCitizenId(userId);
        int verifiedCount = (int) complaintRepository.countByCitizenIdAndStatus(userId, ComplaintStatus.VERIFIED);
        int falseSpamCount = (int) complaintRepository.countByCitizenIdAndStatus(userId, ComplaintStatus.FALSE_SPAM);

        Map<String, Object> riskResult = aiServiceClient.calculateUserRisk(totalSubmitted, verifiedCount, falseSpamCount);

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("userName", user.getName());
        response.put("userMobile", user.getMobile());
        response.put("totalSubmitted", totalSubmitted);
        response.put("verifiedCount", verifiedCount);
        response.put("falseSpamCount", falseSpamCount);
        response.put("accountStatus", user.getAccountStatus());
        response.putAll(riskResult);

        return response;
    }

    @Transactional
    public void blockUser(Long userId, Long adminId, String reason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        user.setAccountStatus("BLOCKED");
        userRepository.save(user);

        BlockedUser blockRecord = new BlockedUser(user, admin, reason);
        blockedUserRepository.save(blockRecord);

        auditLogRepository.save(new AuditLog(admin, "BLOCK_USER", "User", user.getId(), "Blocked citizen " + user.getName() + " (" + user.getMobile() + "). Reason: " + reason));
    }

    @Transactional
    public void unblockUser(Long userId, Long adminId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Super Admin not found"));

        user.setAccountStatus("ACTIVE");
        userRepository.save(user);

        blockedUserRepository.findByUserIdAndUnblockedAtIsNull(userId).ifPresent(record -> {
            record.setUnblockedAt(LocalDateTime.now());
            blockedUserRepository.save(record);
        });

        auditLogRepository.save(new AuditLog(admin, "UNBLOCK_USER", "User", user.getId(), "Unblocked citizen " + user.getName()));
    }
}
