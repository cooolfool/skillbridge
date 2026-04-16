package com.skillbridge.service;

import com.skillbridge.dto.LikeToggleResponse;

public interface LikeProjectService {

    LikeToggleResponse toggleLike(Long projectId, Long userId);

}
