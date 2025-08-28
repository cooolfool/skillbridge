package com.skillbridge.service;

public interface LikeProjectService {

    void likeProject(Long projectId, Long userId);
    void unlikeProject(Long projectId, Long userId);
    long getLikeCount(Long projectId);
    boolean hasUserLiked(Long projectId, Long userId);

}
