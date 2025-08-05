package com.skillbridge.controller;

import com.skillbridge.dto.EditProjectRequest;
import com.skillbridge.dto.ProjectResponse;
import com.skillbridge.dto.PublishProjectRequest;
import com.skillbridge.entity.ProjectEntity;
import com.skillbridge.entity.UserEntity;
import com.skillbridge.service.ProjectService;
import com.skillbridge.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/project", headers = "authToken")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ProjectResponse> publishProject(@Valid @RequestBody PublishProjectRequest publishProjectRequest,
                                                          @RequestHeader(value = "authToken") String token) {

        log.info("Request in publishProject controller");

        UserEntity loggedInUser = userService.loggedInUser(token);
        ProjectResponse response = projectService.publishProject(publishProjectRequest, loggedInUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getProjectsForUser(@RequestHeader(value = "authToken") String token) {

        log.info("Request in getProjectsForUser controller");
        UserEntity loggedInUser = userService.loggedInUser(token);
        return new ResponseEntity<>(projectService.getProjectForUser(loggedInUser), HttpStatus.OK);

    }

    @GetMapping("/feed")
    public ResponseEntity<List<ProjectResponse>> getAllProjectsForFeed(@RequestHeader(value = "authToken") String token) {
        // UserEntity loggedInUser = userService.loggedInUser(token);

        log.info("Request in getAllProjectsForFeed controller");
        return new ResponseEntity<>(projectService.getAllProjectsForFeed(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id, @RequestHeader(value = "authToken") String token) {

        log.info("Request in getProjectById controller");
        ProjectResponse project = projectService.getProjectById(id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @PostMapping("/edit")
    public ResponseEntity<ProjectResponse> editProject(@Valid @RequestBody EditProjectRequest editProjectRequest,
                                                          @RequestHeader(value = "authToken") String token) {

        log.info("Request in editProject controller");

        UserEntity loggedInUser = userService.loggedInUser(token);
        ProjectResponse response = projectService.editProject(editProjectRequest, loggedInUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
