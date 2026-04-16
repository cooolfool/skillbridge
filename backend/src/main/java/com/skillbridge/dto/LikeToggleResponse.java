package com.skillbridge.dto;

import lombok.Data;

@Data
public class LikeToggleResponse {
    boolean liked;
    long likesCount;
}
