package com.skillbridge.service;

import com.skillbridge.dto.AuthResponse;
import com.skillbridge.dto.AuthenticationRequest;
import com.skillbridge.dto.RegisterRequest;
import com.skillbridge.entity.UserEntity;
import com.skillbridge.exception.LoggedInUserException;
import com.skillbridge.exception.ResourceNotFoundException;
import com.skillbridge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        UserEntity user = UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole().toUpperCase())
                .linkedIn(request.getLinkedIn())
                .bio(request.getBio())
                .skills(request.getSkills())
                .gitHub(request.getGitHub())
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).user(user).build();
    }

    public AuthResponse authenticate(AuthenticationRequest request) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        }
        catch(BadCredentialsException e){
            throw new LoggedInUserException("Invalid Credentials!");
        }


        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found with the email id provided."));

        String jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).user(user).build();
    }
}
