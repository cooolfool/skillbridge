package com.skillbridge.dto;

import lombok.Data;

@Data
public class EditProjectRequest {

    private long id;
    private String title;
    private String description;
    private String tags;
    private String repoUrl;

}
