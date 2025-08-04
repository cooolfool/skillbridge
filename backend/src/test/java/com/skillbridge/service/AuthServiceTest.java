package com.skillbridge.service;

import com.skillbridge.dto.AuthResponse;
import com.skillbridge.dto.AuthenticationRequest;
import com.skillbridge.dto.RegisterRequest;
import com.skillbridge.entity.UserEntity;
import com.skillbridge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private AuthenticationRequest loginRequest;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequest.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("Password123!")
                .role("MENTOR")
                .bio("Software Engineer")
                .skills("Java, Spring Boot")
                .gitHub("https://github.com/johndoe")
                .linkedIn("https://linkedin.com/in/johndoe")
                .build();

        loginRequest = AuthenticationRequest.builder()
                .email("john@example.com")
                .password("Password123!")
                .build();

        userEntity = UserEntity.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .password("encodedPassword")
                .role("MENTOR")
                .build();
    }

    @Test
    void register_ShouldCreateNewUserAndReturnAuthResponse() {
        // Arrange
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn("jwtToken");

        // Act
        AuthResponse response = authService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals(userEntity, response.getUser());
        
        verify(passwordEncoder).encode("Password123!");
        verify(userRepository).save(any(UserEntity.class));
        verify(jwtService).generateToken(any(UserEntity.class));
    }

    @Test
    void authenticate_ShouldReturnAuthResponseForValidCredentials() {
        // Arrange
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(userEntity));
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn("jwtToken");

        // Act
        AuthResponse response = authService.authenticate(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals(userEntity, response.getUser());
        
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail("john@example.com");
        verify(jwtService).generateToken(userEntity);
    }

    @Test
    void authenticate_ShouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.authenticate(loginRequest));
        
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail("john@example.com");
        verify(jwtService, never()).generateToken(any());
    }
} 