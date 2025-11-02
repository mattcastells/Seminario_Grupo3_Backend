package com.sip3.backend.auth.controller;

import com.sip3.backend.auth.dto.AuthResponse;
import com.sip3.backend.auth.dto.LoginRequest;
import com.sip3.backend.auth.dto.RegisterRequest;
import com.sip3.backend.auth.service.AuthService;
import com.sip3.backend.user.dto.UserProfileResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Iniciando registro de usuario");
        AuthResponse res = authService.register(request);
        log.info("Register successful for user {}", res.username());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Iniciando login de usuario");
        AuthResponse res = authService.login(request);
        log.info("Login successful for user {}", res.username());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> me(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(authService.me(userDetails.getUsername()));
    }
}
