package com.skillbridge.util;

import com.skillbridge.entity.LikeEntity;
import com.skillbridge.repository.LikeProjectRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProjectUtil {
    private final LikeProjectRepository likeRepository;

    public boolean isProjectLikedByUser(long projectId, long userId) {
        Optional<LikeEntity> existingLike = likeRepository.findByProjectIdAndUserId(projectId, userId);
        if (existingLike.isPresent()) {
            return true;
        }
        return false;
    }
}
