package com.finance.dashboard.controller;

import com.finance.dashboard.dto.CreateUserRequest;
import com.finance.dashboard.dto.LoginRequest;
import com.finance.dashboard.exception.ForbiddenException;
import com.finance.dashboard.exception.ResourceNotFoundException;
import com.finance.dashboard.security.JwtUtil;
import com.finance.dashboard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.finance.dashboard.entity.User;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //User Registration Endpoint
    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    //Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login( @Valid @RequestBody LoginRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ForbiddenException("Incorrect password");
        }

        return ResponseEntity.ok(Map.of(
                "token", jwtUtil.generateToken(user.getId(), user.getRole().name()),
                "role", user.getRole(),
                "userId", user.getId()
        ));
    }
}