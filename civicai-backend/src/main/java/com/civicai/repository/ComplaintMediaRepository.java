package com.civicai.repository;

import com.civicai.entity.ComplaintMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintMediaRepository extends JpaRepository<ComplaintMedia, Long> {
    List<ComplaintMedia> findByComplaintId(Long complaintId);
}
