package com.skillbridge.controller;

import com.skillbridge.dto.CommentDto;
import com.skillbridge.dto.CommentResponseDto;
import com.skillbridge.dto.CommentTreeDto;
import com.skillbridge.entity.UserEntity;
import com.skillbridge.repository.UserRepository;
import com.skillbridge.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    // ---------------- Add Comment ----------------
    @PostMapping
    public ResponseEntity<CommentResponseDto> addComment(
            @RequestBody CommentDto commentDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        log.info("API call: ADD comment to projectId={} by userId={}", commentDto.getProjectId(), user.getId());
        CommentResponseDto response = commentService.addComment(commentDto, user);
        return ResponseEntity.ok(response);
    }

    // ---------------- Edit Comment ----------------
    @PutMapping
    public ResponseEntity<CommentResponseDto> editComment(
            @RequestBody CommentDto commentDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        log.info("API call: EDIT commentId={} by userId={}", commentDto.getId(), user.getId());
        CommentResponseDto response = commentService.editComment(commentDto, user);
        return ResponseEntity.ok(response);
    }

    // ---------------- Delete Comment ----------------
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        log.info("API call: DELETE commentId={} by userId={}", commentId, user.getId());
        CommentResponseDto response = commentService.deleteComment(commentId, user);
        return ResponseEntity.ok(response);
    }

    // ---------------- Fetch Comments for Project ----------------
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<CommentTreeDto>> getCommentsForProject(@PathVariable Long projectId) {
        log.info("API call: GET comments for projectId={}", projectId);
        List<CommentTreeDto> comments = commentService.getCommentsForProject(projectId);
        return ResponseEntity.ok(comments);
    }
}

