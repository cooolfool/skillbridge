package com.skillbridge.controller;


import com.skillbridge.dto.EditProfileRequest;
import com.skillbridge.dto.RegisterRequest;
import com.skillbridge.dto.UserDto;
import com.skillbridge.entity.UserEntity;
import com.skillbridge.exception.ResourceNotFoundException;
import com.skillbridge.service.UserService;
import com.skillbridge.util.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @GetMapping
    ResponseEntity<UserDto> getLoggedInUser(@RequestHeader("Authorization") String authHeader) throws ResourceNotFoundException {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        String token = authHeader.substring(7);
        UserEntity user = userService.loggedInUser(token);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping
    ResponseEntity<UserDto> editProfile(@RequestBody EditProfileRequest editProfileRequest,
                                           @RequestHeader("Authorization") String authHeader){
        UserEntity user = userService.editProfile(editProfileRequest);
        return ResponseEntity.ok(userMapper.toDto(user));

    }

    @GetMapping("/{id}")
    ResponseEntity<UserDto> viewProfile(@PathVariable long id){
        log.info("Request in viewProfile controller for ID : {}", id);
        UserEntity user = userService.viewProfile(id);
        return ResponseEntity.ok(userMapper.toDto(user));
    }


}
