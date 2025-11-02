package com.sip3.backend.auth.service;

import com.sip3.backend.auth.dto.AuthResponse;
import com.sip3.backend.auth.dto.LoginRequest;
import com.sip3.backend.auth.dto.RegisterRequest;
import com.sip3.backend.user.dto.UserProfileResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserProfileResponse me(String username);
}
