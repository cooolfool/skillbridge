package com.skillbridge.service;

import com.skillbridge.dto.EditProfileRequest;
import com.skillbridge.dto.RegisterRequest;
import com.skillbridge.entity.UserEntity;
import com.skillbridge.exception.LoggedInUserException;
import com.skillbridge.exception.ResourceNotFoundException;
import com.skillbridge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    public UserEntity loggedInUser(String token) ;
    public UserEntity editProfile(EditProfileRequest editProfileRequest);
    public UserEntity viewProfile(long id);
}
