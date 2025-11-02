package com.sip3.backend.features.review.controller;

import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.features.review.dto.CreateReviewRequest;
import com.sip3.backend.features.review.dto.ReviewResponse;
import com.sip3.backend.features.review.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
                                              @RequestParam(defaultValue = "20") int size) {
        return reviewService.listByProfessional(professionalId, page, size);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
