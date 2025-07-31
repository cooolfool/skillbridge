package com.skillbridge.dto;

import lombok.Data;

@Data
public class EditProfileRequest {
    private int id;
    private String name;
    private String email;
    private String role;
    private String bio;
    private String skills;
    private String gitHub;
    private String linkedIn;
}
