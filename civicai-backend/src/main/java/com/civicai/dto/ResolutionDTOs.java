package com.civicai.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ResolutionDTOs {

    public static class ResolveComplaintDTO {
        private String resolutionMessage;
        private String afterPhotoBase64;

        public String getResolutionMessage() { return resolutionMessage; }
        public void setResolutionMessage(String resolutionMessage) { this.resolutionMessage = resolutionMessage; }

        public String getAfterPhotoBase64() { return afterPhotoBase64; }
        public void setAfterPhotoBase64(String afterPhotoBase64) { this.afterPhotoBase64 = afterPhotoBase64; }
    }

    public static class VerificationRequestDTO {
        private Boolean isResolved; // true = YES, false = NO (Dispute)
        private String feedback;

        public Boolean getIsResolved() { return isResolved; }
        public void setIsResolved(Boolean isResolved) { this.isResolved = isResolved; }

        public String getFeedback() { return feedback; }
        public void setFeedback(String feedback) { this.feedback = feedback; }
    }

    public static class ResolutionResponseDTO {
        private Long id;
        private String resolutionMessage;
        private String resolvedByName;
        private String resolvedByDepartment;
        private LocalDateTime resolvedAt;
        private String citizenVerificationStatus;
        private String citizenFeedback;
        private List<String> afterMediaPaths;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getResolutionMessage() { return resolutionMessage; }
        public void setResolutionMessage(String resolutionMessage) { this.resolutionMessage = resolutionMessage; }

        public String getResolvedByName() { return resolvedByName; }
        public void setResolvedByName(String resolvedByName) { this.resolvedByName = resolvedByName; }

        public String getResolvedByDepartment() { return resolvedByDepartment; }
        public void setResolvedByDepartment(String resolvedByDepartment) { this.resolvedByDepartment = resolvedByDepartment; }

        public LocalDateTime getResolvedAt() { return resolvedAt; }
        public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

        public String getCitizenVerificationStatus() { return citizenVerificationStatus; }
        public void setCitizenVerificationStatus(String citizenVerificationStatus) { this.citizenVerificationStatus = citizenVerificationStatus; }

        public String getCitizenFeedback() { return citizenFeedback; }
        public void setCitizenFeedback(String citizenFeedback) { this.citizenFeedback = citizenFeedback; }

        public List<String> getAfterMediaPaths() { return afterMediaPaths; }
        public void setAfterMediaPaths(List<String> afterMediaPaths) { this.afterMediaPaths = afterMediaPaths; }
    }
}
