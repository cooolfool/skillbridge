package com.skillbridge.serviceImpl;

import com.skillbridge.dto.CommentDto;
import com.skillbridge.dto.CommentResponseDto;
import com.skillbridge.dto.CommentTreeDto;
import com.skillbridge.entity.CommentEntity;
import com.skillbridge.entity.ProjectEntity;
import com.skillbridge.entity.UserEntity;
import com.skillbridge.exception.LoggedInUserException;
import com.skillbridge.exception.ResourceNotFoundException;
import com.skillbridge.repository.CommentRepository;
import com.skillbridge.repository.ProjectRepository;
import com.skillbridge.repository.UserRepository;
import com.skillbridge.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    // helper mapper
    private CommentDto toDto(CommentEntity e) {
        CommentDto dto = new CommentDto();
        dto.setId(e.getId());
        dto.setProjectId(e.getProject().getId());
        dto.setParentCommentId(e.getParentCommentId());
        dto.setContent(e.getContent());
        dto.setDeleted(e.isDeleted());
        dto.setTimestamp(e.getTimestamp());
        return dto;
    }

    @Transactional
    public CommentResponseDto addComment(CommentDto commentDto, UserEntity user) {
        log.info("Add comment request: projectId={} by userId={}", commentDto.getProjectId(), user.getId());

        ProjectEntity project = projectRepository.findById(commentDto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + commentDto.getProjectId()));

        // If parentCommentId provided, validate parent
        if (commentDto.getParentCommentId() != null) {
            CommentEntity parent = commentRepository.findById(commentDto.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found with id: " + commentDto.getParentCommentId()));
            if (!Objects.equals(parent.getProject().getId(), project.getId())) {
                throw new IllegalArgumentException("Parent comment belongs to a different project");
            }
            if (parent.isDeleted()) {
                throw new IllegalArgumentException("Cannot reply to a deleted comment");
            }
        }

        CommentEntity comment = CommentEntity.builder()
                .content(commentDto.getContent())
                .project(project)
                .user(user)
                .parentCommentId(commentDto.getParentCommentId())
                .build();

        CommentEntity saved = commentRepository.save(comment);

        CommentResponseDto resp = new CommentResponseDto();
        resp.setMessage("Comment added successfully");
        resp.setCommentDto(toDto(saved));
        return resp;
    }

    @Transactional
    public CommentResponseDto editComment(CommentDto commentDto, UserEntity user) {
        log.info("Edit comment request: commentId={} by userId={}", commentDto.getId(), user.getId());

        CommentEntity existing = commentRepository.findById(commentDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentDto.getId()));

        if (existing.isDeleted()) {
            throw new IllegalArgumentException("Cannot edit a deleted comment");
        }

        if (!Objects.equals(existing.getUser().getId(), user.getId())) {
            throw new LoggedInUserException("Not authorized to edit this comment");
        }

        existing.setContent(commentDto.getContent());
        existing.setEditedAt(LocalDateTime.now());
        CommentEntity saved = commentRepository.save(existing);

        CommentResponseDto resp = new CommentResponseDto();
        resp.setMessage("Comment edited successfully");
        resp.setCommentDto(toDto(saved));
        return resp;
    }

    @Transactional
    public CommentResponseDto deleteComment(Long commentId, UserEntity user) {
        log.info("Delete comment request: commentId={} by userId={}", commentId, user.getId());

        CommentEntity existing = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        //Comment owner and Project Owners can delete a comment
        ProjectEntity project = existing.getProject();

        boolean isOwner = Objects.equals(existing.getUser().getId(), user.getId());
        boolean isProjectOwner = Objects.equals(project.getCreatedBy().getId(), user.getId());

        if (!isOwner && !isProjectOwner){
            throw new LoggedInUserException("Not authorized to delete this comment");
        }
        // recursively soft delete parent + all children
        softDeleteRecursive(existing);

        CommentResponseDto resp = new CommentResponseDto();
        resp.setMessage("Comment deleted successfully");
        resp.setCommentDto(toDto(existing));
        return resp;
    }

    private void softDeleteRecursive(CommentEntity comment) {
        // soft delete current
        markCommentDeleted(comment);

        // fetch children
        List<CommentEntity> childComments = commentRepository.findByParentCommentId(comment.getId());

        // recursively delete each child
        for (CommentEntity child : childComments) {
            softDeleteRecursive(child);
        }
    }


    public CommentEntity markCommentDeleted(CommentEntity comment){
        comment.setDeleted(true);
        comment.setEditedAt(LocalDateTime.now());
        CommentEntity saved = commentRepository.save(comment);
        return saved;
    }


    public List<CommentTreeDto> getCommentsForProject(Long projectId) {
        log.info("Fetching comments for projectId={}", projectId);
        List<CommentEntity> topLevel = commentRepository.findByProjectId(projectId);

        // Step 2: map entities -> DTOs with recursion
        return topLevel.stream()
                .map(this::toDtoWithChildren)
                .collect(Collectors.toList());
    }

    private CommentTreeDto toDtoWithChildren(CommentEntity entity) {
        CommentTreeDto dto = toTreeDto(entity);

        // Step 3: fetch children
        List<CommentEntity> children = commentRepository.findByParentCommentId(entity.getId());

        dto.setReplies(children.stream()
                .map(this::toDtoWithChildren) // recursion
                .collect(Collectors.toList()));

        return dto;
    }

    private CommentTreeDto toTreeDto(CommentEntity entity) {
        CommentTreeDto dto = new CommentTreeDto();
        dto.setId(entity.getId());
        dto.setProjectId(entity.getProject().getId());
        dto.setParentCommentId(entity.getParentCommentId());
        dto.setContent(entity.isDeleted() ? "This comment was deleted" : entity.getContent());
        dto.setDeleted(entity.isDeleted());
        dto.setTimestamp(entity.getTimestamp());
        dto.setEditedAt(entity.getEditedAt());

        // Add user info
        dto.setUserId(entity.getUser().getId());
        dto.setUserName(entity.getUser().getName());

        return dto;
    }



}
