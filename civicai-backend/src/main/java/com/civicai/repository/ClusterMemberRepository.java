package com.civicai.repository;

import com.civicai.entity.ClusterMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClusterMemberRepository extends JpaRepository<ClusterMember, Long> {
    List<ClusterMember> findByClusterId(Long clusterId);
    Optional<ClusterMember> findByComplaintId(Long complaintId);
}
