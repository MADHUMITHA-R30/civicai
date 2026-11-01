package com.civicai.repository;

import com.civicai.entity.ResolutionMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResolutionMediaRepository extends JpaRepository<ResolutionMedia, Long> {
    List<ResolutionMedia> findByResolutionId(Long resolutionId);
}
