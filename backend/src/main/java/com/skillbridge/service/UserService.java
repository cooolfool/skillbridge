package com.skillbridge.service;

import com.skillbridge.entity.UserEntity;
import com.skillbridge.exception.ResourceNotFoundException;
import com.skillbridge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public UserEntity loggedInUser(String token) throws ResourceNotFoundException {
        String email = jwtService.extractUsername(token);
        Optional<UserEntity> loggedInUser = userRepository.findByEmail(email);
        if(loggedInUser.isPresent()){
            return loggedInUser.get();
        }
        throw new ResourceNotFoundException("Invalid Login");
    }
}
