package com.skillbridge.serviceImpl;

import com.skillbridge.dto.EditProfileRequest;
import com.skillbridge.entity.UserEntity;
import com.skillbridge.exception.LoggedInUserException;
import com.skillbridge.exception.ResourceNotFoundException;
import com.skillbridge.repository.UserRepository;
import com.skillbridge.service.JwtService;
import com.skillbridge.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {


   private final UserRepository userRepository;

    private final JwtService jwtService;

    public UserEntity loggedInUser(String token)  {

        log.info("Request in loggedInUser service");
        String email = jwtService.extractUsername(token);
        log.info("User email : {}",email);
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

    public UserEntity viewProfile(long id){
        log.info("Request in viewProfile service for ID : {}", id);
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not found!"));
    }

}
