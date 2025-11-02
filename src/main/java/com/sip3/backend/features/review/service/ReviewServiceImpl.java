package com.sip3.backend.features.review.service;

import com.sip3.backend.common.exception.NotFoundException;
import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.features.professional.model.ProfessionalProfile;
import com.sip3.backend.features.professional.repository.ProfessionalRepository;
import com.sip3.backend.features.review.dto.CreateReviewRequest;
import com.sip3.backend.features.review.dto.ReviewResponse;
import com.sip3.backend.features.review.model.Review;
import com.sip3.backend.features.review.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ProfessionalRepository professionalRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             ReviewMapper reviewMapper,
                             ProfessionalRepository professionalRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.professionalRepository = professionalRepository;
    }

    @Override
    public ReviewResponse create(String userId, String userDisplayName, CreateReviewRequest request) {
        ProfessionalProfile profile = professionalRepository.findById(request.professionalId())
                .orElseThrow(() -> new NotFoundException("Profesional no encontrado"));
        Review review = reviewMapper.toEntity(userId, userDisplayName, request);
        Review saved = reviewRepository.save(review);
        updateProfessionalRating(profile, request.rating());
        return reviewMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ReviewResponse> listByProfessional(String professionalId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> result = reviewRepository.findByProfessionalId(professionalId, pageable);
        return new PagedResponse<>(
                result.getContent().stream().map(reviewMapper::toResponse).collect(Collectors.toList()),
                result.getTotalElements(),
                result.getTotalPages(),
                result.getNumber(),
                result.getSize()
        );
    }

    @Override
    public void delete(String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Reseña no encontrada"));
        reviewRepository.delete(review);
        ProfessionalProfile profile = professionalRepository.findById(review.getProfessionalId())
                .orElse(null);
        if (profile != null) {
            recalculateProfessionalRating(profile);
        }
    }

    private void updateProfessionalRating(ProfessionalProfile profile, int newRating) {
        double currentAverage = profile.getRating() == null ? 0.0 : profile.getRating();
        int totalReviews = profile.getReviewsCount() == null ? 0 : profile.getReviewsCount();
        double updatedAverage = ((currentAverage * totalReviews) + newRating) / (totalReviews + 1);
        profile.setRating(Math.round(updatedAverage * 10.0) / 10.0);
        profile.setReviewsCount(totalReviews + 1);
        professionalRepository.save(profile);
    }

    private void recalculateProfessionalRating(ProfessionalProfile profile) {
        Page<Review> allReviews = reviewRepository.findByProfessionalId(profile.getId(), Pageable.unpaged());
        int total = (int) allReviews.getTotalElements();
        if (total == 0) {
            profile.setRating(null);
            profile.setReviewsCount(0);
        } else {
            double average = allReviews.getContent().stream().mapToInt(Review::getRating).average().orElse(0.0);
            profile.setRating(Math.round(average * 10.0) / 10.0);
            profile.setReviewsCount(total);
        }
        professionalRepository.save(profile);
    }
}
