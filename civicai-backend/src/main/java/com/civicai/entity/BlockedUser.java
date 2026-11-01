package com.civicai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "blocked_users")
public class BlockedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "blocked_by", nullable = false)
    private User blockedBy;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String reason;

    @Column(name = "blocked_at", nullable = false)
    private LocalDateTime blockedAt = LocalDateTime.now();

    @Column(name = "unblocked_at")
    private LocalDateTime unblockedAt;

    public BlockedUser() {}

    public BlockedUser(User user, User blockedBy, String reason) {
        this.user = user;
        this.blockedBy = blockedBy;
        this.reason = reason;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public User getBlockedBy() { return blockedBy; }
    public void setBlockedBy(User blockedBy) { this.blockedBy = blockedBy; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDateTime getBlockedAt() { return blockedAt; }
    public void setBlockedAt(LocalDateTime blockedAt) { this.blockedAt = blockedAt; }

    public LocalDateTime getUnblockedAt() { return unblockedAt; }
    public void setUnblockedAt(LocalDateTime unblockedAt) { this.unblockedAt = unblockedAt; }
}
