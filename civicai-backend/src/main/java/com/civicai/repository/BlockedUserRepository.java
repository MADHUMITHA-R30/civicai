package com.civicai.repository;

import com.civicai.entity.BlockedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockedUserRepository extends JpaRepository<BlockedUser, Long> {
    Optional<BlockedUser> findByUserIdAndUnblockedAtIsNull(Long userId);
    Boolean existsByUserIdAndUnblockedAtIsNull(Long userId);
}
