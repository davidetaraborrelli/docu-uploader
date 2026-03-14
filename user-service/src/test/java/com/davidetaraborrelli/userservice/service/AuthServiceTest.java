package com.davidetaraborrelli.userservice.service;

import com.davidetaraborrelli.common.security.JwtUtils;
import com.davidetaraborrelli.userservice.dto.LoginRequest;
import com.davidetaraborrelli.userservice.dto.RegisterRequest;
import com.davidetaraborrelli.userservice.dto.AuthResponse;
import com.davidetaraborrelli.userservice.entity.User;
import com.davidetaraborrelli.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_success() {
        RegisterRequest request = new RegisterRequest("testuser", "test@test.com", "password123");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });
        when(jwtUtils.generateToken(1L, "testuser", "test@test.com")).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        assertEquals("jwt-token", response.token());
        assertEquals("testuser", response.username());
    }

    @Test
    void register_duplicateUsername_throws() {
        RegisterRequest request = new RegisterRequest("testuser", "test@test.com", "password123");
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    void login_success() {
        LoginRequest request = new LoginRequest("testuser", "password123");

        User user = new User("testuser", "test@test.com", "hashed");
        user.setId(1L);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashed")).thenReturn(true);
        when(jwtUtils.generateToken(1L, "testuser", "test@test.com")).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        assertEquals("jwt-token", response.token());
    }

    @Test
    void login_wrongPassword_throws() {
        LoginRequest request = new LoginRequest("testuser", "wrong");

        User user = new User("testuser", "test@test.com", "hashed");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> authService.login(request));
    }

    @Test
    void login_userNotFound_throws() {
        LoginRequest request = new LoginRequest("nonexistent", "password");
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> authService.login(request));
    }
}
