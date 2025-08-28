package com.skillbridge.serviceImpl;

import com.skillbridge.entity.LikeEntity;
import com.skillbridge.entity.ProjectEntity;
import com.skillbridge.entity.UserEntity;
import com.skillbridge.repository.LikeProjectRepository;
import com.skillbridge.repository.ProjectRepository;
import com.skillbridge.repository.UserRepository;
import com.skillbridge.service.LikeProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
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

    @Override
    public void likeProject(Long projectId, Long userId) {
        if (likeRepository.existsByProjectIdAndUserId(projectId, userId)) {
            return; // already liked
        }

        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        likeRepository.save(LikeEntity.builder()
                .project(project)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build());
        project.setLikesCount(project.getLikesCount()+1);
        projectRepository.save(project);

        // Increment like count
        redisTemplate.opsForValue().increment(getLikeCountKey(projectId));
        redisTemplate.expire(getLikeCountKey(projectId), CACHE_TTL);
    }

    @Override
    public void unlikeProject(Long projectId, Long userId) {
        Optional<LikeEntity> existing = likeRepository.findByProjectIdAndUserId(projectId, userId);
        if (existing.isEmpty()) return;
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        likeRepository.delete(existing.get());
        project.setLikesCount(project.getLikesCount()-1);
        projectRepository.save(project);
        // Decrement like count
        redisTemplate.opsForValue().decrement(getLikeCountKey(projectId));
        redisTemplate.expire(getLikeCountKey(projectId), CACHE_TTL);
    }

    @Override
    public long getLikeCount(Long projectId) {
        String key = getLikeCountKey(projectId);

        String cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return Long.parseLong(cached);
        }

        long count = likeRepository.countByProjectId(projectId);
        redisTemplate.opsForValue().set(key, String.valueOf(count), CACHE_TTL);
        return count;
    }

    @Override
    public boolean hasUserLiked(Long projectId, Long userId) {
        return likeRepository.existsByProjectIdAndUserId(projectId, userId);
    }
}
