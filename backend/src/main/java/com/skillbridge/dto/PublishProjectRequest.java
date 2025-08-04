package com.skillbridge.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PublishProjectRequest {

    @NotBlank(message = "Project title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    
    @NotBlank(message = "Project description is required")
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    private String description;
    
    @NotBlank(message = "Project tags are required")
    @Size(max = 200, message = "Tags cannot exceed 200 characters")
    private String tags;
    
    @NotBlank(message = "Repository URL is required")
    @Pattern(regexp = "^(https?://)?(www\\.)?github\\.com/[a-zA-Z0-9-]+/[a-zA-Z0-9-]+/?$", 
             message = "Repository URL must be a valid GitHub repository URL")
    private String repoUrl;
}
