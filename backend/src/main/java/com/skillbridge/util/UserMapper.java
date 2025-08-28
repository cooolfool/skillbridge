package com.skillbridge.util;

import com.skillbridge.dto.UserDto;
import com.skillbridge.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private UserMapper() {}

    public static UserDto toDto(UserEntity user) {
        if (user == null) return null;

        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getBio(),
                user.getRole(),
                user.getSkills(),
                user.getLinkedIn(),
                user.getGitHub()
        );
    }
}
