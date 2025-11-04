package com.sip3.backend.auth.service;

import com.sip3.backend.auth.dto.AuthResponse;
import com.sip3.backend.auth.dto.LoginRequest;
import com.sip3.backend.auth.dto.RegisterRequest;
import com.sip3.backend.auth.jwt.JwtTokenProvider;
import com.sip3.backend.common.exception.BadRequestException;
import com.sip3.backend.user.dto.UserProfileResponse;
import com.sip3.backend.user.model.Role;
import com.sip3.backend.user.model.User;
import com.sip3.backend.user.model.UserProfile;
import com.sip3.backend.user.repository.UserRepository;
import com.sip3.backend.user.service.UserMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserMapper userMapper;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider tokenProvider,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userMapper = userMapper;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsernameIgnoreCase(request.username())) {
            throw new BadRequestException("El usuario ya existe");
        }
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new BadRequestException("El correo ya está registrado");
        }

        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        if (request.registerAsProfessional()) {
            roles.add(Role.PROFESSIONAL);
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .phone(request.phone())
                .password(passwordEncoder.encode(request.password()))
                .roles(roles)
                .profile(UserProfile.builder()
                        .fullName(request.fullName())
                        .avatarUrl("https://res.cloudinary.com/dtjbknm5h/image/upload/v1762223757/user_hzfwna.jpg")
                        .build())
                .build();

        userRepository.save(user);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        Set<String> roleNames = roles.stream().map(Role::name).collect(java.util.stream.Collectors.toSet());
        return new AuthResponse(token, request.username(), roleNames);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        User user = userRepository.findByUsernameIgnoreCase(request.username())
                .or(() -> userRepository.findByEmailIgnoreCase(request.username()))
                .orElseThrow(() -> new BadRequestException("Credenciales inválidas"));

        Set<String> roles = user.getRoles().stream().map(Role::name).collect(java.util.stream.Collectors.toSet());
        return new AuthResponse(token, user.getUsername(), roles);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse me(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .or(() -> userRepository.findByEmailIgnoreCase(username))
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));
        return userMapper.toProfileResponse(user);
    }
}
