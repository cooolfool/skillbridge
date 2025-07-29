package com.skillbridge.dto;

import com.skillbridge.entity.CommentEntity;
import com.skillbridge.entity.LikeEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ProjectResponse {

    private Long id;
    private String title;
    private String description;
    private String tags;
    private String repoUrl;
    private LocalDateTime createdAt;
    private Set<CommentEntity> comments;
    private Set<LikeEntity> likes;

}
