package com.civicai.repository;

import com.civicai.entity.Complaint;
import com.civicai.enums.ComplaintStatus;
import com.civicai.enums.PriorityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Optional<Complaint> findByComplaintNumber(String complaintNumber);
    List<Complaint> findByCitizenIdOrderByCreatedAtDesc(Long citizenId);
    List<Complaint> findByDepartmentIdOrderByCreatedAtDesc(Long departmentId);
    List<Complaint> findByDepartmentIdAndStatus(Long departmentId, ComplaintStatus status);
    List<Complaint> findByStatus(ComplaintStatus status);
    
    long countByCitizenId(Long citizenId);
    long countByCitizenIdAndStatus(Long citizenId, ComplaintStatus status);
    long countByDepartmentId(Long departmentId);
    long countByDepartmentIdAndStatus(Long departmentId, ComplaintStatus status);
    
    @Query("SELECT c FROM Complaint c WHERE c.department.id = :deptId AND (:status IS NULL OR c.status = :status) AND (:priority IS NULL OR c.priority = :priority) ORDER BY c.createdAt DESC")
    List<Complaint> findByDepartmentFiltered(@Param("deptId") Long deptId, @Param("status") ComplaintStatus status, @Param("priority") PriorityLevel priority);

    @Query("SELECT c FROM Complaint c WHERE (:deptId IS NULL OR c.department.id = :deptId) AND (:status IS NULL OR c.status = :status) ORDER BY c.createdAt DESC")
    List<Complaint> findAllFiltered(@Param("deptId") Long deptId, @Param("status") ComplaintStatus status);
}
