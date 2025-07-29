package com.skillbridge.repository;

import com.skillbridge.entity.ProjectEntity;
import com.skillbridge.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity,Long> {

    List<ProjectEntity> findByUser(UserEntity user);

}
