

package com.skillbridge.repository;

import com.skillbridge.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeProjectRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByProjectIdAndUserId(Long projectId, Long userId);
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);
    long countByProjectId(Long projectId);
}

