package com.skillbridge.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "projects")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProjectEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity createdBy;


    @Column(columnDefinition = "TEXT")
    private String title;


    @Column(columnDefinition = "TEXT")
    private String description;


    @Column(columnDefinition = "TEXT")
    private String tags;

    private String repoUrl;
    private boolean isArchived;
    private boolean isDeleted;

    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<CommentEntity> comments;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<LikeEntity> likes;
}