package com.skillbridge.repository;

import com.skillbridge.entity.ProjectEntity;
import com.skillbridge.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity,Long> {

    List<ProjectEntity> findByCreatedBy(UserEntity user);
    Optional<ProjectEntity> findById(Long id);

}
