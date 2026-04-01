package com.skillbridge.service;

import com.skillbridge.dto.CommentDto;
import com.skillbridge.dto.CommentResponseDto;
import com.skillbridge.dto.CommentTreeDto;
import com.skillbridge.entity.UserEntity;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CommentService {
    /**
     * Add a new comment to a project or as a reply to another comment
     */
    CommentResponseDto addComment(CommentDto commentDto, UserEntity user);

    /**
     * Edit an existing comment
     */
    CommentResponseDto editComment(CommentDto commentDto, UserEntity user);

    /**
     * Soft delete a comment and all its children recursively
     */
    CommentResponseDto deleteComment(Long commentId, UserEntity user);

    /**
     * Fetch all comments for a project as a nested tree
     */
    List<CommentTreeDto> getCommentsForProject(Long projectId);
}
