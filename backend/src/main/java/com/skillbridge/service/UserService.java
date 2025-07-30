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
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;
    public UserEntity loggedInUser(String token)  {
        String email = jwtService.extractUsername(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new LoggedInUserException("Invalid Login"));
    }


    public UserEntity editProfile(EditProfileRequest editProfileRequest){

        Optional<UserEntity> user = userRepository.findByEmail(editProfileRequest.getEmail());
        if(user.isPresent()){
            UserEntity userEntity = user.get();
            BeanUtils.copyProperties(editProfileRequest,userEntity,"id");
            userRepository.save(userEntity);
            return userEntity;
        }
        throw new ResourceNotFoundException("User not found");
    }
}
