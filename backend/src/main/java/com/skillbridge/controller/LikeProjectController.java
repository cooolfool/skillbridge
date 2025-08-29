package com.skillbridge.controller;

import com.skillbridge.entity.UserEntity;
import com.skillbridge.service.LikeProjectService;
import com.skillbridge.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Slf4j
public class LikeProjectController {

    private final LikeProjectService likeProjectService;
    private final UserService userService;

    @PostMapping("/{projectId}/like")
    public ResponseEntity<String> likeProject(@PathVariable Long projectId,
                                              @RequestHeader(value = "authToken") String token) {
        log.info("Request in like project controller");
        log.info("Request Hearders : {}",token);
        UserEntity loggedInUser = userService.loggedInUser(token);
        log.info("Logged in user : {}", loggedInUser.getName());
        likeProjectService.likeProject(projectId, loggedInUser.getId());
        return ResponseEntity.ok("Project liked successfully");
    }

    @PostMapping("/{projectId}/unlike")
    public ResponseEntity<Map<String,String>> unlikeProject(@PathVariable Long projectId,
                                                            @RequestHeader(value = "authToken") String token) {
        log.info("Request in unlike project controller");
        log.info("Request Hearders : {}",token);
        UserEntity loggedInUser = userService.loggedInUser(token);
        likeProjectService.unlikeProject(projectId, loggedInUser.getId());
        return ResponseEntity.ok(Map.of("message", "Project liked successfully"));
    }

    @GetMapping("/{projectId}/likes/count")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long projectId) {
        log.info("Request in get likes count");
        return ResponseEntity.ok(likeProjectService.getLikeCount(projectId));
    }

    @GetMapping("/{projectId}/likes/status")
    public ResponseEntity<Boolean> hasUserLiked(@PathVariable Long projectId,
                                                @RequestHeader("authToken") String token) {
        log.info("Request in get liked status");
        log.info("Request Hearders : {}",token);
        UserEntity loggedInUser = userService.loggedInUser(token);
        return ResponseEntity.ok(likeProjectService.hasUserLiked(projectId, loggedInUser.getId()));
    }
}
