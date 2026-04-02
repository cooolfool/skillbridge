package com.skillbridge.dto;

import lombok.Data;

@Data
public class CommentResponseDto {
    private String message;
    private CommentDto commentDto;
}
