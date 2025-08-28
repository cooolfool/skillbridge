package com.skillbridge.dto;


import org.springframework.stereotype.Component;


public record UserDto(
        Long id,
        String name,
        String email,
        String bio,
        String role,
        String skills,
        String linkedIn,
        String gitHub
) {}
