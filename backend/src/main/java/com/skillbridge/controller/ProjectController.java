package com.skillbridge.controller;


import com.skillbridge.dto.ProjectResponse;
import com.skillbridge.dto.PublishProjectRequest;
import com.skillbridge.entity.ProjectEntity;
import com.skillbridge.entity.UserEntity;
import com.skillbridge.service.ProjectService;
import com.skillbridge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/project", headers = "authToken")
public class ProjectController {

    @Autowired
    ProjectService projectService;
    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<ProjectResponse> publishProject(@RequestBody PublishProjectRequest publishProjectRequest,
                                                          @RequestHeader(value = "authToken") String token) {

        UserEntity loggedInUser = userService.loggedInUser(token);
        ProjectResponse response = projectService.publishProject(publishProjectRequest, loggedInUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getProjectsForUser(@RequestHeader(value = "authToken") String token) {

        UserEntity loggedInUser = userService.loggedInUser(token);
        return new ResponseEntity<>(projectService.getProjectForUser(loggedInUser), HttpStatus.OK);

    }

    @GetMapping("/feed")
        public ResponseEntity<List<ProjectResponse>> getAllProjectsForFeed(@RequestHeader(value = "authToken") String token) {
        UserEntity loggedInUser = userService.loggedInUser(token);
        return new ResponseEntity<>(projectService.getAllProjectsForFeed(), HttpStatus.OK);

    }
}
