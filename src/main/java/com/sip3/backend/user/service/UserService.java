package com.sip3.backend.user.service;

import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.user.dto.UserProfileResponse;
import com.sip3.backend.user.dto.UserProfileUpdateRequest;

public interface UserService {

    PagedResponse<UserProfileResponse> findAll(int page, int size);

    UserProfileResponse getById(String id);

    UserProfileResponse updateProfileByUsername(String username, UserProfileUpdateRequest request);
}
