package com.skillbridge.service;

import com.skillbridge.dto.EditProjectRequest;
import com.skillbridge.dto.ProjectResponse;
import com.skillbridge.dto.PublishProjectRequest;
import com.skillbridge.entity.UserEntity;

import java.util.List;

public interface ProjectService {

    public ProjectResponse publishProject(PublishProjectRequest publishProjectRequest, UserEntity loggedInUser);
    public List<ProjectResponse> getProjectForUser(UserEntity user);
    public List<ProjectResponse> getAllProjectsForFeed();
    public ProjectResponse getProjectById(Long id);
    public ProjectResponse editProject(EditProjectRequest editProjectRequest,UserEntity loggedInUser);

}
