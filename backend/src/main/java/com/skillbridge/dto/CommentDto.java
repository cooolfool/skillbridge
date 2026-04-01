package com.skillbridge.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private Long projectId;          // renamed for clarity
    private Long parentCommentId;    // nullable
    private String content;
    private Boolean deleted;
    private LocalDateTime timestamp;
}
