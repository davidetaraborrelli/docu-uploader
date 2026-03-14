package com.davidetaraborrelli.userservice.controller;

import com.davidetaraborrelli.common.dto.UserInfo;
import com.davidetaraborrelli.userservice.dto.AuthResponse;
import com.davidetaraborrelli.userservice.dto.LoginRequest;
import com.davidetaraborrelli.userservice.dto.RegisterRequest;
import com.davidetaraborrelli.userservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/me")
    public ResponseEntity<Map<String, Object>> me(Authentication authentication) {
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();
        return ResponseEntity.ok(Map.of(
                "id", userInfo.id(),
                "username", userInfo.username(),
                "email", userInfo.email()
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}
