package com.sip3.backend.features.review.repository;

import com.sip3.backend.features.review.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, String> {

    Page<Review> findByProfessionalId(String professionalId, Pageable pageable);
    
    List<Review> findByProfessionalId(String professionalId);
    
    Optional<Review> findByUserIdAndProfessionalId(String userId, String professionalId);
    
    boolean existsByUserIdAndProfessionalId(String userId, String professionalId);
}
