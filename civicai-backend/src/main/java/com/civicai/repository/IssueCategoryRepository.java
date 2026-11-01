package com.civicai.repository;

import com.civicai.entity.IssueCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface IssueCategoryRepository extends JpaRepository<IssueCategory, Long> {
    Optional<IssueCategory> findByName(String name);
    List<IssueCategory> findByActiveTrue();
    List<IssueCategory> findByDefaultDepartmentId(Long departmentId);
}
