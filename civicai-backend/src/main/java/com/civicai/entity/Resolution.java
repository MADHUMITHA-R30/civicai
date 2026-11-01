package com.civicai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resolutions")
public class Resolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "complaint_id", nullable = false)
    private Complaint complaint;

    @Column(name = "resolution_message", columnDefinition = "TEXT", nullable = false)
    private String resolutionMessage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resolved_by", nullable = false)
    private User resolvedBy;

    @Column(name = "resolved_at", nullable = false)
    private LocalDateTime resolvedAt = LocalDateTime.now();

    @Column(name = "citizen_verification_status", nullable = false)
    private String citizenVerificationStatus = "PENDING"; // PENDING, VERIFIED, DISPUTED

    @Column(name = "citizen_feedback", columnDefinition = "TEXT")
    private String citizenFeedback;

    public Resolution() {}

    public Resolution(Complaint complaint, String resolutionMessage, User resolvedBy) {
        this.complaint = complaint;
        this.resolutionMessage = resolutionMessage;
        this.resolvedBy = resolvedBy;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Complaint getComplaint() { return complaint; }
    public void setComplaint(Complaint complaint) { this.complaint = complaint; }

    public String getResolutionMessage() { return resolutionMessage; }
    public void setResolutionMessage(String resolutionMessage) { this.resolutionMessage = resolutionMessage; }

    public User getResolvedBy() { return resolvedBy; }
    public void setResolvedBy(User resolvedBy) { this.resolvedBy = resolvedBy; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

    public String getCitizenVerificationStatus() { return citizenVerificationStatus; }
    public void setCitizenVerificationStatus(String citizenVerificationStatus) { this.citizenVerificationStatus = citizenVerificationStatus; }

    public String getCitizenFeedback() { return citizenFeedback; }
    public void setCitizenFeedback(String citizenFeedback) { this.citizenFeedback = citizenFeedback; }
}
