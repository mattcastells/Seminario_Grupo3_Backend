package com.sip3.backend.user.service;

import com.sip3.backend.user.dto.UserProfileResponse;
import com.sip3.backend.user.dto.UserProfileUpdateRequest;
import com.sip3.backend.user.model.User;
import com.sip3.backend.user.model.UserProfile;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserProfileResponse toProfileResponse(User user) {
        UserProfile profile = user.getProfile();
        Set<String> roles = user.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                profile != null ? profile.getFullName() : null,
                profile != null ? profile.getLocation() : null,
                profile != null ? profile.getAvatarUrl() : null,
                profile != null ? profile.getPreferredPaymentMethods() : null,
                roles,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public void updateProfile(User user, UserProfileUpdateRequest request) {
        UserProfile profile = user.getProfile();
        if (profile == null) {
            profile = new UserProfile();
            user.setProfile(profile);
        }
        profile.setFullName(request.fullName());
        profile.setLocation(request.location());
        profile.setPhone(request.phone());
        profile.setEmail(request.email());
        profile.setAvatarUrl(request.avatarUrl());
        profile.setPreferredPaymentMethods(request.preferredPaymentMethods());

        if (!Objects.equals(user.getEmail(), request.email())) {
            user.setEmail(request.email());
        }
        if (!Objects.equals(user.getPhone(), request.phone())) {
            user.setPhone(request.phone());
        }
    }
}
