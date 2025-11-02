package com.sip3.backend.user.service;

import com.sip3.backend.common.exception.NotFoundException;
import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.user.dto.UserProfileResponse;
import com.sip3.backend.user.dto.UserProfileUpdateRequest;
import com.sip3.backend.user.model.User;
import com.sip3.backend.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<UserProfileResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> result = userRepository.findAll(pageable);
        return new PagedResponse<>(
                result.getContent().stream().map(userMapper::toProfileResponse).collect(Collectors.toList()),
                result.getTotalElements(),
                result.getTotalPages(),
                result.getNumber(),
                result.getSize()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        return userMapper.toProfileResponse(user);
    }

    @Override
    public UserProfileResponse updateProfileByUsername(String username, UserProfileUpdateRequest request) {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .or(() -> userRepository.findByEmailIgnoreCase(username))
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        userMapper.updateProfile(user, request);
        userRepository.save(user);
        return userMapper.toProfileResponse(user);
    }
}
