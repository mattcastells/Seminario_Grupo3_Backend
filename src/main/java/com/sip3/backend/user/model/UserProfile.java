package com.sip3.backend.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    private String fullName;
    private String location;
    private String phone;
    private String email;
    private List<String> preferredPaymentMethods;
    private String avatarUrl;
}
