package com.skillbridge.controller;


import com.skillbridge.entity.UserEntity;
import com.skillbridge.exception.ResourceNotFoundException;
import com.skillbridge.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    ResponseEntity<UserEntity> getLoggedInUser( @RequestHeader("Authorization") String authHeader) throws ResourceNotFoundException {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }

        String token = authHeader.substring(7);
        UserEntity user = userService.loggedInUser(token);
        return ResponseEntity.ok(user);
    }

}
