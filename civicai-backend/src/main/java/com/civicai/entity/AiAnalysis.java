package com.civicai.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ai_analysis")
public class AiAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_id", nullable = false)
    private Complaint complaint;

    @Column(name = "extracted_category")
    private String extractedCategory;

    @Column(name = "extracted_address")
    private String extractedAddress;

    @Column(name = "extracted_contact")
    private String extractedContact;

    private String severity;
    
    @Column(name = "priority_score")
    private Integer priorityScore;

    @Column(name = "department_prediction")
    private String departmentPrediction;

    private Double confidence;

    @Column(name = "duplicate_probability")
    private Double duplicateProbability;

    @Column(name = "priority_explanation", columnDefinition = "TEXT")
    private String priorityExplanation;

    public AiAnalysis() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Complaint getComplaint() { return complaint; }
    public void setComplaint(Complaint complaint) { this.complaint = complaint; }

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
