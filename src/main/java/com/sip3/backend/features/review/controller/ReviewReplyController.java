package com.sip3.backend.features.review.controller;

import com.sip3.backend.features.review.dto.CreateReplyRequest;
import com.sip3.backend.features.review.dto.ReviewReplyResponse;
import com.sip3.backend.features.review.dto.UpdateReplyRequest;
import com.sip3.backend.features.review.service.ReviewReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/review-replies")
@RequiredArgsConstructor
public class ReviewReplyController {

    private final ReviewReplyService replyService;

    @PostMapping
    public ResponseEntity<ReviewReplyResponse> create(@AuthenticationPrincipal UserDetails userDetails,
                                                      @Valid @RequestBody CreateReplyRequest request) {
        String userId = userDetails.getUsername();
        String userName = userDetails.getUsername(); // Podrías mejorar esto para usar el nombre real
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(replyService.create(userId, userName, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewReplyResponse> update(@AuthenticationPrincipal UserDetails userDetails,
                                                      @PathVariable String id,
                                                      @Valid @RequestBody UpdateReplyRequest request) {
        String userId = userDetails.getUsername();
        return ResponseEntity.ok(replyService.update(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable String id) {
        String userId = userDetails.getUsername();
        replyService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-review/{reviewId}")
    public ResponseEntity<ReviewReplyResponse> getByReviewId(@PathVariable String reviewId) {
        return replyService.findByReviewId(reviewId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/check/{reviewId}")
    public ResponseEntity<Boolean> hasReply(@PathVariable String reviewId) {
        return ResponseEntity.ok(replyService.hasReply(reviewId));
    }
}

