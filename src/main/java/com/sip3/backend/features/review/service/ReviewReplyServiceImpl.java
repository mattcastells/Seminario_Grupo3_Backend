package com.sip3.backend.features.review.service;

import com.sip3.backend.common.exception.BadRequestException;
import com.sip3.backend.common.exception.NotFoundException;
import com.sip3.backend.features.review.dto.CreateReplyRequest;
import com.sip3.backend.features.review.dto.ReviewReplyResponse;
import com.sip3.backend.features.review.dto.UpdateReplyRequest;
import com.sip3.backend.features.review.model.Review;
import com.sip3.backend.features.review.model.ReviewReply;
import com.sip3.backend.features.review.repository.ReviewReplyRepository;
import com.sip3.backend.features.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewReplyServiceImpl implements ReviewReplyService {

    private final ReviewReplyRepository replyRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public ReviewReplyResponse create(String professionalId, String professionalName, CreateReplyRequest request) {
        // Verificar que la review existe
        Review review = reviewRepository.findById(request.reviewId())
                .orElseThrow(() -> new NotFoundException("Reseña no encontrada"));

        // Verificar que el profesional es el dueño de la review (fue calificado)
        if (!review.getProfessionalId().equals(professionalId)) {
            throw new BadRequestException("Solo podés responder a reviews que te hicieron a vos");
        }

        // Verificar que no haya respondido ya
        if (replyRepository.existsByReviewId(request.reviewId())) {
            throw new BadRequestException("Ya respondiste a esta reseña");
        }

        ReviewReply reply = ReviewReply.builder()
                .reviewId(request.reviewId())
                .professionalId(professionalId)
                .professionalName(professionalName)
                .reply(request.reply())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        ReviewReply saved = replyRepository.save(reply);
        return mapToResponse(saved);
    }

    @Override
    public ReviewReplyResponse update(String professionalId, String replyId, UpdateReplyRequest request) {
        ReviewReply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new NotFoundException("Respuesta no encontrada"));

        // Verificar que el profesional sea el dueño de la respuesta
        if (!reply.getProfessionalId().equals(professionalId)) {
            throw new BadRequestException("No podés editar una respuesta que no es tuya");
        }

        reply.setReply(request.reply());
        reply.setUpdatedAt(Instant.now());

        ReviewReply updated = replyRepository.save(reply);
        return mapToResponse(updated);
    }

    @Override
    public void delete(String professionalId, String replyId) {
        ReviewReply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new NotFoundException("Respuesta no encontrada"));

        // Verificar que el profesional sea el dueño de la respuesta
        if (!reply.getProfessionalId().equals(professionalId)) {
            throw new BadRequestException("No podés eliminar una respuesta que no es tuya");
        }

        replyRepository.delete(reply);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReviewReplyResponse> findByReviewId(String reviewId) {
        return replyRepository.findByReviewId(reviewId)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasReply(String reviewId) {
        return replyRepository.existsByReviewId(reviewId);
    }

    private ReviewReplyResponse mapToResponse(ReviewReply reply) {
        return new ReviewReplyResponse(
                reply.getId(),
                reply.getReviewId(),
                reply.getProfessionalId(),
                reply.getProfessionalName(),
                reply.getReply(),
                reply.getCreatedAt(),
                reply.getUpdatedAt()
        );
    }
}

