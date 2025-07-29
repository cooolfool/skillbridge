package com.skillbridge.dto;

import lombok.Data;

@Data
public class PublishProjectRequest {

    //All fields should be mandatory
    private String title;
    private String description;
    private String tags;
    private String repoUrl;
}
