package com.sip3.backend.features.review.service;

import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.features.review.dto.CreateReviewRequest;
import com.sip3.backend.features.review.dto.ReviewResponse;

public interface ReviewService {

    ReviewResponse create(String userId, String userDisplayName, CreateReviewRequest request);

    PagedResponse<ReviewResponse> listByProfessional(String professionalId, int page, int size);

    void delete(String reviewId);
}
