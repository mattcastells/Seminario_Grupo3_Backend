package com.sip3.backend.features.review.repository;

import com.sip3.backend.features.review.model.ReviewReply;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewReplyRepository extends MongoRepository<ReviewReply, String> {

    Optional<ReviewReply> findByReviewId(String reviewId);

    boolean existsByReviewId(String reviewId);

    void deleteByReviewId(String reviewId);
}

