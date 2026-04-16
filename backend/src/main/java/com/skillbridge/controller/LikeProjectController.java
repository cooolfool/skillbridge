package com.skillbridge.controller;

import com.skillbridge.dto.LikeToggleResponse;
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


    @PostMapping("/{id}/like-toggle")
    public ResponseEntity<LikeToggleResponse> toggleLikeProject(@PathVariable("id") Long projectId,
                                                                @RequestHeader(value = "authToken") String token) {
        log.info("Request in like project controller");
        log.info("Request Hearders : {}", token);
        UserEntity loggedInUser = userService.loggedInUser(token);
        log.info("Logged in user : {}", loggedInUser.getName());
        LikeToggleResponse response = likeProjectService.toggleLike(projectId, loggedInUser.getId());
        log.info("Project liked successfully!");
        log.info("Returning from controller : {}", ResponseEntity.ok("Project liked successfully"));
        return ResponseEntity.ok(response);
    }
}