package com.civicai.dto;

import java.util.List;
import java.util.Map;

public class AnalyticsDTOs {

    public static class DepartmentAnalyticsDTO {
        private String departmentName;
        private long totalIssues;
        private long newIssues;
        private long inProgressIssues;
        private long criticalIssues;
        private long resolvedIssues;
        private long duplicateCount;
        private long falseSpamCount;
        private double resolutionRatePercentage;
        private double avgResolutionTimeDays;
        private double satisfactionRating;

        private Map<String, Long> issuesByCategory;
        private Map<String, Long> issuesByPriority;
        private Map<String, Long> issuesByStatus;
        private List<HotspotDTO> hotspots;
        private List<RecurringIssueDTO> recurringIssues;

        public String getDepartmentName() { return departmentName; }
        public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

        public long getTotalIssues() { return totalIssues; }
        public void setTotalIssues(long totalIssues) { this.totalIssues = totalIssues; }

        public long getNewIssues() { return newIssues; }
        public void setNewIssues(long newIssues) { this.newIssues = newIssues; }

        public long getInProgressIssues() { return inProgressIssues; }
        public void setInProgressIssues(long inProgressIssues) { this.inProgressIssues = inProgressIssues; }

        public long getCriticalIssues() { return criticalIssues; }
        public void setCriticalIssues(long criticalIssues) { this.criticalIssues = criticalIssues; }

        public long getResolvedIssues() { return resolvedIssues; }
        public void setResolvedIssues(long resolvedIssues) { this.resolvedIssues = resolvedIssues; }

        public long getDuplicateCount() { return duplicateCount; }
        public void setDuplicateCount(long duplicateCount) { this.duplicateCount = duplicateCount; }

        public long getFalseSpamCount() { return falseSpamCount; }
        public void setFalseSpamCount(long falseSpamCount) { this.falseSpamCount = falseSpamCount; }

        public double getResolutionRatePercentage() { return resolutionRatePercentage; }
        public void setResolutionRatePercentage(double resolutionRatePercentage) { this.resolutionRatePercentage = resolutionRatePercentage; }

        public double getAvgResolutionTimeDays() { return avgResolutionTimeDays; }
        public void setAvgResolutionTimeDays(double avgResolutionTimeDays) { this.avgResolutionTimeDays = avgResolutionTimeDays; }

        public double getSatisfactionRating() { return satisfactionRating; }
        public void setSatisfactionRating(double satisfactionRating) { this.satisfactionRating = satisfactionRating; }

        public Map<String, Long> getIssuesByCategory() { return issuesByCategory; }
        public void setIssuesByCategory(Map<String, Long> issuesByCategory) { this.issuesByCategory = issuesByCategory; }

        public Map<String, Long> getIssuesByPriority() { return issuesByPriority; }
        public void setIssuesByPriority(Map<String, Long> issuesByPriority) { this.issuesByPriority = issuesByPriority; }

        public Map<String, Long> getIssuesByStatus() { return issuesByStatus; }
        public void setIssuesByStatus(Map<String, Long> issuesByStatus) { this.issuesByStatus = issuesByStatus; }

        public List<HotspotDTO> getHotspots() { return hotspots; }
        public void setHotspots(List<HotspotDTO> hotspots) { this.hotspots = hotspots; }

        public List<RecurringIssueDTO> getRecurringIssues() { return recurringIssues; }
        public void setRecurringIssues(List<RecurringIssueDTO> recurringIssues) { this.recurringIssues = recurringIssues; }
    }

    public static class HotspotDTO {
        private String locationName;
        private Double latitude;
        private Double longitude;
        private int complaintCount;
        private String primaryCategory;
        private String riskLevel;

        public HotspotDTO(String locationName, Double latitude, Double longitude, int complaintCount, String primaryCategory, String riskLevel) {
            this.locationName = locationName;
            this.latitude = latitude;
            this.longitude = longitude;
            this.complaintCount = complaintCount;
            this.primaryCategory = primaryCategory;
            this.riskLevel = riskLevel;
        }

        public String getLocationName() { return locationName; }
        public Double getLatitude() { return latitude; }
        public Double getLongitude() { return longitude; }
        public int getComplaintCount() { return complaintCount; }
        public String getPrimaryCategory() { return primaryCategory; }
        public String getRiskLevel() { return riskLevel; }
    }

    public static class RecurringIssueDTO {
        private String locationName;
        private String category;
        private int historicalCount;
        private String timeFrame;
        private String aiRecommendation;

        public RecurringIssueDTO(String locationName, String category, int historicalCount, String timeFrame, String aiRecommendation) {
            this.locationName = locationName;
            this.category = category;
            this.historicalCount = historicalCount;
            this.timeFrame = timeFrame;
            this.aiRecommendation = aiRecommendation;
        }

        public String getLocationName() { return locationName; }
        public String getCategory() { return category; }
        public int getHistoricalCount() { return historicalCount; }
        public String getTimeFrame() { return timeFrame; }
        public String getAiRecommendation() { return aiRecommendation; }
    }
}
