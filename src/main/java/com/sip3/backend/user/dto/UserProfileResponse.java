package com.sip3.backend.user.dto;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public record UserProfileResponse(
        String id,
        String username,
        String email,
        String phone,
        String fullName,
        String location,
        String avatarUrl,
        List<String> preferredPaymentMethods,
        Set<String> roles,
        Instant createdAt,
        Instant updatedAt
) {
}
