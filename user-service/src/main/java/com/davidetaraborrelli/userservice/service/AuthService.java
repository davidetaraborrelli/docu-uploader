package com.davidetaraborrelli.userservice.service;

import com.davidetaraborrelli.common.security.JwtUtils;
import com.davidetaraborrelli.userservice.dto.AuthResponse;
import com.davidetaraborrelli.userservice.dto.LoginRequest;
import com.davidetaraborrelli.userservice.dto.RegisterRequest;
import com.davidetaraborrelli.userservice.entity.User;
import com.davidetaraborrelli.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username già in uso");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email già in uso");
        }

        User user = new User(
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password())
        );
        user = userRepository.save(user);

        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getEmail());
        return new AuthResponse(token, user.getUsername(), user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Credenziali non valide"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Credenziali non valide");
        }

        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getEmail());
        return new AuthResponse(token, user.getUsername(), user.getEmail());
    }
}
