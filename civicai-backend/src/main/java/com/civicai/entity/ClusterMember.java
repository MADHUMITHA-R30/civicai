package com.civicai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "complaint_cluster_members")
public class ClusterMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cluster_id", nullable = false)
    private ComplaintCluster cluster;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "complaint_id", nullable = false)
    private Complaint complaint;

    @Column(name = "similarity_score")
    private Double similarityScore;

    @Column(name = "linked_at", nullable = false)
    private LocalDateTime linkedAt = LocalDateTime.now();

    public ClusterMember() {}

    public ClusterMember(ComplaintCluster cluster, Complaint complaint, Double similarityScore) {
        this.cluster = cluster;
        this.complaint = complaint;
        this.similarityScore = similarityScore;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ComplaintCluster getCluster() { return cluster; }
    public void setCluster(ComplaintCluster cluster) { this.cluster = cluster; }

    public Complaint getComplaint() { return complaint; }
    public void setComplaint(Complaint complaint) { this.complaint = complaint; }

    public Double getSimilarityScore() { return similarityScore; }
    public void setSimilarityScore(Double similarityScore) { this.similarityScore = similarityScore; }

    public LocalDateTime getLinkedAt() { return linkedAt; }
    public void setLinkedAt(LocalDateTime linkedAt) { this.linkedAt = linkedAt; }
}
