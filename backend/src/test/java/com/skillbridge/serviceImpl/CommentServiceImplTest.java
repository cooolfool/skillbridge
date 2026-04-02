package com.skillbridge.serviceImpl;

import com.skillbridge.dto.CommentDto;
import com.skillbridge.dto.CommentResponseDto;
import com.skillbridge.entity.CommentEntity;
import com.skillbridge.entity.ProjectEntity;
import com.skillbridge.entity.UserEntity;
import com.skillbridge.exception.LoggedInUserException;
import com.skillbridge.exception.ResourceNotFoundException;
import com.skillbridge.repository.CommentRepository;
import com.skillbridge.repository.ProjectRepository;
import com.skillbridge.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    // ---------------- ADD COMMENT ----------------

    @Test
    void addComment_success() {
        CommentDto dto = new CommentDto();
        dto.setContent("Test comment");
        dto.setProjectId(1L);

        UserEntity user = new UserEntity();
        user.setId(10L);

        ProjectEntity project = new ProjectEntity();
        project.setId(1L);

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(commentRepository.save(any(CommentEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CommentResponseDto response = commentService.addComment(dto, user);

        assertEquals("Comment added successfully", response.getMessage());
        assertEquals("Test comment", response.getCommentDto().getContent());
    }

    @Test
    void addComment_projectNotFound_shouldThrow() {
        CommentDto dto = new CommentDto();
        dto.setProjectId(1L);

        UserEntity user = new UserEntity();

        when(projectRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.addComment(dto, user));
    }

    // ---------------- EDIT COMMENT ----------------

    @Test
    void editComment_success() {
        CommentDto dto = new CommentDto();
        dto.setId(1L);
        dto.setContent("Updated");

        UserEntity user = new UserEntity();
        user.setId(1L);

        ProjectEntity project = new ProjectEntity();
        project.setId(100L);

        CommentEntity existing = new CommentEntity();
        existing.setId(1L);
        existing.setUser(user);
        existing.setProject(project);
        existing.setDeleted(false);

        when(commentRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(commentRepository.save(any(CommentEntity.class)))
                .thenReturn(existing);

        CommentResponseDto response = commentService.editComment(dto, user);

        assertEquals("Comment edited successfully", response.getMessage());
        assertEquals("Updated", existing.getContent());
    }

    @Test
    void editComment_notOwner_shouldThrow() {
        CommentDto dto = new CommentDto();
        dto.setId(1L);

        UserEntity loggedInUser = new UserEntity();
        loggedInUser.setId(2L);

        UserEntity owner = new UserEntity();
        owner.setId(1L);

        CommentEntity existing = new CommentEntity();
        existing.setUser(owner);
        existing.setDeleted(false);

        when(commentRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        assertThrows(LoggedInUserException.class,
                () -> commentService.editComment(dto, loggedInUser));
    }

    @Test
    void editComment_deletedComment_shouldThrow() {
        CommentDto dto = new CommentDto();
        dto.setId(1L);

        UserEntity user = new UserEntity();
        user.setId(1L);

        CommentEntity existing = new CommentEntity();
        existing.setUser(user);
        existing.setDeleted(true);

        when(commentRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class,
                () -> commentService.editComment(dto, user));
    }

    // ---------------- DELETE COMMENT ----------------

    @Test
    void deleteComment_owner_success() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        ProjectEntity project = new ProjectEntity();
        UserEntity projectOwner = new UserEntity();
        projectOwner.setId(2L);
        project.setCreatedBy(projectOwner);

        CommentEntity comment = new CommentEntity();
        comment.setId(1L);
        comment.setUser(user);
        comment.setProject(project);

        when(commentRepository.findById(1L))
                .thenReturn(Optional.of(comment));

        when(commentRepository.findByParentCommentId(anyLong()))
                .thenReturn(List.of());

        when(commentRepository.save(any(CommentEntity.class)))
                .thenReturn(comment);

        CommentResponseDto response = commentService.deleteComment(1L, user);

        assertEquals("Comment deleted successfully", response.getMessage());
        assertTrue(comment.isDeleted());
    }

    @Test
    void deleteComment_notAuthorized_shouldThrow() {
        UserEntity user = new UserEntity();
        user.setId(3L);

        UserEntity owner = new UserEntity();
        owner.setId(1L);

        ProjectEntity project = new ProjectEntity();
        UserEntity projectOwner = new UserEntity();
        projectOwner.setId(2L);
        project.setCreatedBy(projectOwner);

        CommentEntity comment = new CommentEntity();
        comment.setUser(owner);
        comment.setProject(project);

        when(commentRepository.findById(1L))
                .thenReturn(Optional.of(comment));

        assertThrows(LoggedInUserException.class,
                () -> commentService.deleteComment(1L, user));
    }

    // ---------------- FETCH COMMENTS ----------------

    @Test
    void getCommentsForProject_shouldReturnTree() {
        ProjectEntity project = new ProjectEntity();
        project.setId(1L);

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("Test User");

        CommentEntity comment = new CommentEntity();
        comment.setId(1L);
        comment.setProject(project);
        comment.setUser(user);
        comment.setDeleted(false);

        when(commentRepository.findByProjectId(1L))
                .thenReturn(List.of(comment));

        when(commentRepository.findByParentCommentId(1L))
                .thenReturn(List.of());

        var result = commentService.getCommentsForProject(1L);

        assertEquals(1, result.size());
        assertEquals("Test User", result.get(0).getUserName());
    }
}