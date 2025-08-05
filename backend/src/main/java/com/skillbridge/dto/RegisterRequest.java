package com.skillbridge.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", 
             message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;
    
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(MENTOR|MENTEE)$", message = "Role must be either MENTOR or MENTEE")
    private String role; // MENTOR or MENTEE
    
    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;
    
    @Size(max = 200, message = "Skills cannot exceed 200 characters")
    private String skills;
    
    @Pattern(regexp = "^(https?://)?(www\\.)?github\\.com/[a-zA-Z0-9-]+/?$", 
             message = "GitHub URL must be a valid GitHub profile URL")
    private String gitHub;
    
    @Pattern(regexp = "^(https?://)?(www\\.)?linkedin\\.com/in/[a-zA-Z0-9-]+/?$", 
             message = "LinkedIn URL must be a valid LinkedIn profile URL")
    private String linkedIn;
}
