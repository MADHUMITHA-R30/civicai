package com.civicai.repository;

import com.civicai.entity.UserFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFlagRepository extends JpaRepository<UserFlag, Long> {
    List<UserFlag> findByUserId(Long userId);
    List<UserFlag> findByComplaintId(Long complaintId);
    long countByUserId(Long userId);
}
