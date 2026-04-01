package com.skillbridge.repository;

import com.skillbridge.entity.CommentEntity;
import com.skillbridge.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {

    Optional<CommentEntity> findById(Long id);

    List<CommentEntity> findByProjectAndDeletedFalse(ProjectEntity project);
    List<CommentEntity> findByProjectIdAndDeletedFalse(Long projectId);
    List<CommentEntity> findByProjectIdAndParentCommentId(Long projectId, Long parentCommentId);
    List<CommentEntity> findByParentCommentId(Long parentCommentId);

}
