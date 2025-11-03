package com.sip3.backend.features.review.service;

import com.sip3.backend.features.review.dto.CreateReplyRequest;
import com.sip3.backend.features.review.dto.ReviewReplyResponse;
import com.sip3.backend.features.review.dto.UpdateReplyRequest;

import java.util.Optional;

public interface ReviewReplyService {

    ReviewReplyResponse create(String professionalId, String professionalName, CreateReplyRequest request);

    ReviewReplyResponse update(String professionalId, String replyId, UpdateReplyRequest request);

    void delete(String professionalId, String replyId);

    Optional<ReviewReplyResponse> findByReviewId(String reviewId);

    boolean hasReply(String reviewId);
}

