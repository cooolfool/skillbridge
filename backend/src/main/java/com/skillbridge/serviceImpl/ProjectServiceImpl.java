package com.skillbridge.serviceImpl;

import com.skillbridge.dto.ProjectResponse;
import com.skillbridge.dto.PublishProjectRequest;
import com.skillbridge.entity.ProjectEntity;
import com.skillbridge.entity.UserEntity;
import com.skillbridge.exception.FieldLevelException;
import com.skillbridge.repository.ProjectRepository;
import com.skillbridge.service.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    ProjectRepository projectRepo;

    @Override
    public ProjectResponse publishProject(PublishProjectRequest publishProjectRequest, UserEntity loggedInUser) {

        Map<String,Object> errorMap = new HashMap<>();
        errorMap = validatePublishProjectRequest(publishProjectRequest);
        if(null == loggedInUser){
            errorMap.put("Logged In User","User not validated");
        }

        if(!errorMap.isEmpty()){
            throw new FieldLevelException("Error Validating Fields",errorMap);
        }
        ProjectEntity project = new ProjectEntity();
        BeanUtils.copyProperties(publishProjectRequest,project);
        project.setCreatedAt(LocalDateTime.now());
        project.setArchived(false);
        project.setDeleted(false);
        project.setUser(loggedInUser);
        projectRepo.save(project);

        ProjectResponse response = new ProjectResponse();
        BeanUtils.copyProperties(project,response);
        return response;
    }

    @Override
    public List<ProjectResponse> getProjectForUser(UserEntity user) {
        List<ProjectEntity> projectEntityList = projectRepo.findByUser(user);
        List<ProjectResponse> responseList = new ArrayList<>();
        for(ProjectEntity project : projectEntityList){
            ProjectResponse projectResponse = new ProjectResponse();
            BeanUtils.copyProperties(project,projectResponse);
            responseList.add(projectResponse);
        }
        return responseList;
    }

    @Override
    public List<ProjectResponse> getAllProjectsForFeed() {
        List<ProjectEntity> projectEntityList = projectRepo.findAll();
        List<ProjectResponse> responseList = new ArrayList<>();
        for(ProjectEntity project : projectEntityList){
            ProjectResponse projectResponse = new ProjectResponse();
            BeanUtils.copyProperties(project,projectResponse);
            responseList.add(projectResponse);
        }
        return responseList;
    }

    public Map<String, Object> validatePublishProjectRequest(PublishProjectRequest publishProjectRequest){

        Map<String, Object> errorMap = new HashMap<>();
        if(null == publishProjectRequest.getDescription()){
            errorMap.put("description","Project Description cannot be empty");
        }

        if(null == publishProjectRequest.getTags()){
            errorMap.put("tags","Project must have a tag");
        }

        if(null == publishProjectRequest.getRepoUrl()){
            errorMap.put("repoUrl","Project must have a repository link");
        }

        if(null == publishProjectRequest.getTitle()){
            errorMap.put("title", "Project must have a title");
        }

        return errorMap;
    }
}
