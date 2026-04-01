package com.skillbridge.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentTreeDto {
    private Long id;
    private Long projectId;
    private Long parentCommentId;
    private String content;
    private boolean isDeleted;
    private LocalDateTime timestamp;
    private LocalDateTime editedAt;

    // User info
    private Long userId;
    private String userName;

    private List<CommentTreeDto> replies;
}
