package com.app.ecommerce.controller;

import com.app.ecommerce.dto.*;
import com.app.ecommerce.entity.User;
import com.app.ecommerce.security.JwtUtil;
import com.app.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // ===== REGISTER =====
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        
        // Create new user
        User user = new User(
            request.getName(),
            request.getEmail(),
            request.getPassword(),
            "USER" // Default role
        );
        
        User savedUser = userService.register(user);
        
        // Generate JWT token
        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole());
        
        AuthResponse response = new AuthResponse(
            token,
            savedUser.getEmail(),
            savedUser.getName(),
            savedUser.getRole()
        );
        
        return ResponseEntity.ok(response);
    }

    // ===== LOGIN =====
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        
        // Find user by email
        User user = userService.findByEmail(request.getEmail());
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        
        AuthResponse response = new AuthResponse(
            token,
            user.getEmail(),
            user.getName(),
            user.getRole()
        );
        
        return ResponseEntity.ok(response);
    }

    // ===== GET CURRENT USER (Protected) =====
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        
        String token = authHeader.substring(7); // Remove "Bearer "
        String email = jwtUtil.extractEmail(token);
        
        User user = userService.findByEmail(email);
        
        UserResponse response = new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole()
        );
        
        return ResponseEntity.ok(response);
    }
}