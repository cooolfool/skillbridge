package com.skillbridge.serviceImpl;

import com.skillbridge.dto.EditProjectRequest;
import com.skillbridge.dto.ProjectResponse;
import com.skillbridge.dto.PublishProjectRequest;
import com.skillbridge.entity.ProjectEntity;
import com.skillbridge.entity.UserEntity;
import com.skillbridge.exception.FieldLevelException;
import com.skillbridge.exception.LoggedInUserException;
import com.skillbridge.exception.ResourceNotFoundException;
import com.skillbridge.repository.ProjectRepository;
import com.skillbridge.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {


    @Autowired
    ProjectRepository projectRepo;

    @Override
    public ProjectResponse publishProject(PublishProjectRequest publishProjectRequest, UserEntity loggedInUser) {

        log.info("Request in publishProject service");
        Map<String, Object> errorMap = new HashMap<>();
        errorMap = validatePublishProjectRequest(publishProjectRequest);
        if (null == loggedInUser) {
            errorMap.put("Logged In User", "User not validated");
        }

        if (!errorMap.isEmpty()) {
            throw new FieldLevelException("Error Validating Fields", errorMap);
        }
        ProjectEntity project = new ProjectEntity();
        BeanUtils.copyProperties(publishProjectRequest, project);
        project.setCreatedAt(LocalDateTime.now());
        project.setArchived(false);
        project.setDeleted(false);
        project.setCreatedBy(loggedInUser);
        projectRepo.save(project);

        ProjectResponse response = new ProjectResponse();
        BeanUtils.copyProperties(project, response);
        return response;
    }

    @Override
    public List<ProjectResponse> getProjectForUser(UserEntity user) {
        log.info("Request in getProjectForUser service");
        List<ProjectEntity> projectEntityList = projectRepo.findByCreatedBy(user);
        List<ProjectResponse> responseList = new ArrayList<>();
        for (ProjectEntity project : projectEntityList) {
            if (project.getCreatedBy().equals(user)) {
                ProjectResponse projectResponse = new ProjectResponse();
                BeanUtils.copyProperties(project, projectResponse);
                responseList.add(projectResponse);
            }
        }
        return responseList;
    }

    @Override
    public List<ProjectResponse> getAllProjectsForFeed() {
        log.info("Request in getAllProjectsForFeed service");
        List<ProjectEntity> projectEntityList = projectRepo.findAll();
        List<ProjectResponse> responseList = new ArrayList<>();
        for (ProjectEntity project : projectEntityList) {
            ProjectResponse projectResponse = new ProjectResponse();
            BeanUtils.copyProperties(project, projectResponse);
            responseList.add(projectResponse);
        }
        return responseList;
    }

    @Override
    public ProjectResponse getProjectById(Long id) {
        log.info("Request in getProjectById service");
        Optional<ProjectEntity> optionalProject = projectRepo.findById(id);
        if(optionalProject.isPresent()){
            ProjectResponse response = new ProjectResponse();
            BeanUtils.copyProperties(optionalProject.get(),response);
            return response;
        }
       throw new ResourceNotFoundException("No Project found with given id");
    }

    @Override
    public ProjectResponse editProject(EditProjectRequest editProjectRequest,UserEntity loggedInUser) {
        log.info("Request in editProject service");
        ProjectEntity project = projectRepo.findById(editProjectRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No project found"));

        if (!project.getCreatedBy().equals(loggedInUser)) {
            throw new LoggedInUserException("Not Authorized");
        }
        BeanUtils.copyProperties(editProjectRequest, project, "id");
        projectRepo.save(project);

        ProjectResponse response = new ProjectResponse();
        BeanUtils.copyProperties(project, response);
        return response;

    }

    public Map<String, Object> validatePublishProjectRequest(PublishProjectRequest publishProjectRequest) {

        Map<String, Object> errorMap = new HashMap<>();
        if (null == publishProjectRequest.getDescription()) {
            errorMap.put("description", "Project Description cannot be empty");
        }

        if (null == publishProjectRequest.getTags()) {
            errorMap.put("tags", "Project must have a tag");
        }

        if (null == publishProjectRequest.getRepoUrl()) {
            errorMap.put("repoUrl", "Project must have a repository link");
        }

        if (null == publishProjectRequest.getTitle()) {
            errorMap.put("title", "Project must have a title");
        }

        return errorMap;
    }
}
