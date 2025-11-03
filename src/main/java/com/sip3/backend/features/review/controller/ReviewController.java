package com.sip3.backend.features.review.controller;

import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.features.review.dto.CreateReviewRequest;
import com.sip3.backend.features.review.dto.ReviewResponse;
import com.sip3.backend.features.review.dto.ReviewSortBy;
import com.sip3.backend.features.review.dto.UpdateReviewRequest;
import com.sip3.backend.features.review.service.ReviewService;
import org.springframework.data.domain.Sort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> create(@AuthenticationPrincipal UserDetails userDetails,
                                                 @Valid @RequestBody CreateReviewRequest request) {
        String username = userDetails.getUsername();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.create(username, username, request));
    }

    @GetMapping
    public PagedResponse<ReviewResponse> list(@RequestParam String professionalId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "20") int size,
                                              @RequestParam(required = false) ReviewSortBy sortBy,
                                              @RequestParam(required = false) Sort.Direction order,
                                              @RequestParam(required = false) Integer filterByRating) {
        return reviewService.listByProfessional(professionalId, page, size, sortBy, order, filterByRating);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> update(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable String id,
                                                 @Valid @RequestBody UpdateReviewRequest request) {
        String userId = userDetails.getUsername();
        return ResponseEntity.ok(reviewService.update(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable String id) {
        String userId = userDetails.getUsername();
        reviewService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkIfUserReviewed(@AuthenticationPrincipal UserDetails userDetails,
                                                        @RequestParam String professionalId) {
        String userId = userDetails.getUsername();
        boolean hasReviewed = reviewService.hasUserReviewedProfessional(userId, professionalId);
        return ResponseEntity.ok(hasReviewed);
    }

    @GetMapping("/user-review")
    public ResponseEntity<ReviewResponse> getUserReview(@AuthenticationPrincipal UserDetails userDetails,
                                                         @RequestParam String professionalId) {
        String userId = userDetails.getUsername();
        return reviewService.findUserReviewForProfessional(userId, professionalId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
