package com.skillbridge.serviceImpl;

import com.skillbridge.dto.LikeToggleResponse;
import com.skillbridge.entity.LikeEntity;
import com.skillbridge.entity.ProjectEntity;
import com.skillbridge.entity.UserEntity;
import com.skillbridge.repository.LikeProjectRepository;
import com.skillbridge.repository.ProjectRepository;
import com.skillbridge.repository.UserRepository;
import com.skillbridge.service.LikeProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LikeProjectServiceImpl implements LikeProjectService {

    private final LikeProjectRepository likeRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String LIKE_KEY_PREFIX = "project:%d:likes";
    private static final Duration CACHE_TTL = Duration.ofHours(6);

    private String getLikeCountKey(Long projectId) {
        return String.format(LIKE_KEY_PREFIX, projectId);
    }

    public LikeToggleResponse toggleLike(Long projectId, Long userId){
        LikeToggleResponse likeToggleResponse = new LikeToggleResponse();
        Optional<LikeEntity> existingLike = likeRepository.findByProjectIdAndUserId(projectId, userId);
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        boolean isLiked = false;
        if (existingLike.isPresent()) {
            log.info("the project : {} is already liked by the user : {}", projectId,userId);
            likeRepository.delete(existingLike.get());
            project.setLikesCount(project.getLikesCount()-1);
            projectRepository.save(project);
            // Decrement like count
            redisTemplate.opsForValue().decrement(getLikeCountKey(projectId));
            redisTemplate.expire(getLikeCountKey(projectId), CACHE_TTL);
            isLiked = false;
        }
        else{
            likeRepository.save(LikeEntity.builder()
                    .project(project)
                    .user(user)
                    .createdAt(LocalDateTime.now())
                    .build());
            project.setLikesCount(project.getLikesCount()+1);
            projectRepository.save(project);
            log.info("Liked project!");
            // Increment like count
            redisTemplate.opsForValue().increment(getLikeCountKey(projectId));
            redisTemplate.expire(getLikeCountKey(projectId), CACHE_TTL);
            isLiked = true;
        }
        likeToggleResponse.setLiked(isLiked);
        likeToggleResponse.setLikesCount(project.getLikesCount());
        return likeToggleResponse;
    }
}
