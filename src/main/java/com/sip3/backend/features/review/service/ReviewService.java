package com.sip3.backend.features.review.service;

import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.features.review.dto.CreateReviewRequest;
import com.sip3.backend.features.review.dto.ReviewResponse;
import com.sip3.backend.features.review.dto.ReviewSortBy;
import com.sip3.backend.features.review.dto.UpdateReviewRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public interface ReviewService {

    ReviewResponse create(String userId, String userDisplayName, CreateReviewRequest request);

    ReviewResponse update(String userId, String reviewId, UpdateReviewRequest request);

    PagedResponse<ReviewResponse> listByProfessional(String professionalId, int page, int size, 
                                                     ReviewSortBy sortBy, Sort.Direction order, Integer filterByRating);

    void delete(String userId, String reviewId);
    
    Optional<ReviewResponse> findUserReviewForProfessional(String userId, String professionalId);
    
    boolean hasUserReviewedProfessional(String userId, String professionalId);
}
