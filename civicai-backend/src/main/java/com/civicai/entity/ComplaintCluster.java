package com.civicai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "complaint_clusters")
public class ComplaintCluster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cluster_reference", nullable = false, unique = true)
    private String clusterReference;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "primary_complaint_id", nullable = false)
    private Complaint primaryComplaint;

    @Column(nullable = false)
    private String title;

    @Column(name = "report_count", nullable = false)
    private Integer reportCount = 1;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ComplaintCluster() {}

    public ComplaintCluster(String clusterReference, Complaint primaryComplaint, String title) {
        this.clusterReference = clusterReference;
        this.primaryComplaint = primaryComplaint;
        this.title = title;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClusterReference() { return clusterReference; }
    public void setClusterReference(String clusterReference) { this.clusterReference = clusterReference; }

    public Complaint getPrimaryComplaint() { return primaryComplaint; }
    public void setPrimaryComplaint(Complaint primaryComplaint) { this.primaryComplaint = primaryComplaint; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getReportCount() { return reportCount; }
    public void setReportCount(Integer reportCount) { this.reportCount = reportCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
