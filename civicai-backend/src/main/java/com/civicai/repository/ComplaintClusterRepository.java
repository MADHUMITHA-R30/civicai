package com.civicai.repository;

import com.civicai.entity.ComplaintCluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComplaintClusterRepository extends JpaRepository<ComplaintCluster, Long> {
    Optional<ComplaintCluster> findByClusterReference(String clusterReference);
    Optional<ComplaintCluster> findByPrimaryComplaintId(Long primaryComplaintId);
}
