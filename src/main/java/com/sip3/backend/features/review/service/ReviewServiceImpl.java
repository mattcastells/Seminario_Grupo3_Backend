package com.sip3.backend.features.review.service;

import com.sip3.backend.common.exception.BadRequestException;
import com.sip3.backend.common.exception.NotFoundException;
import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.features.professional.model.ProfessionalProfile;
import com.sip3.backend.features.professional.repository.ProfessionalRepository;
import com.sip3.backend.features.review.dto.CreateReviewRequest;
import com.sip3.backend.features.review.dto.ReviewResponse;
import com.sip3.backend.features.review.dto.ReviewSortBy;
import com.sip3.backend.features.review.dto.UpdateReviewRequest;
import com.sip3.backend.features.review.model.Review;
import com.sip3.backend.features.review.repository.ReviewRepository;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ProfessionalRepository professionalRepository;
    private final MongoTemplate mongoTemplate;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             ReviewMapper reviewMapper,
                             ProfessionalRepository professionalRepository,
                             MongoTemplate mongoTemplate) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.professionalRepository = professionalRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ReviewResponse create(String userId, String userDisplayName, CreateReviewRequest request) {
        // Validar que el usuario no haya dejado ya una review para este profesional
        if (reviewRepository.existsByUserIdAndProfessionalId(userId, request.professionalId())) {
            throw new BadRequestException("Ya dejaste una reseña para este profesional");
        }
        
        ProfessionalProfile profile = professionalRepository.findById(request.professionalId())
                .orElseThrow(() -> new NotFoundException("Profesional no encontrado"));
        Review review = reviewMapper.toEntity(userId, userDisplayName, request);
        Review saved = reviewRepository.save(review);
        updateProfessionalRating(profile, request.rating());
        return reviewMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ReviewResponse> listByProfessional(String professionalId, int page, int size,
                                                            ReviewSortBy sortBy, Sort.Direction order, Integer filterByRating) {
        // Construir query base
        Query query = new Query();
        query.addCriteria(Criteria.where("professionalId").is(professionalId));

        // Aplicar filtro por rating si está presente
        if (filterByRating != null && filterByRating >= 1 && filterByRating <= 5) {
            query.addCriteria(Criteria.where("rating").is(filterByRating));
        }

        // Contar total de documentos que cumplen el criterio
        long total = mongoTemplate.count(query, Review.class);

        // Aplicar ordenamiento
        Sort sort = buildSort(sortBy, order);
        query.with(sort);

        // Aplicar paginación
        Pageable pageable = PageRequest.of(page, size);
        query.with(pageable);

        // Ejecutar query
        List<Review> reviews = mongoTemplate.find(query, Review.class);

        // Mapear a respuesta
        List<ReviewResponse> content = reviews.stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());

        int totalPages = (int) Math.ceil((double) total / size);

        return new PagedResponse<>(content, total, totalPages, page, size);
    }

    private Sort buildSort(ReviewSortBy sortBy, Sort.Direction order) {
        // Por defecto: ordenar por fecha descendente (más recientes primero)
        if (sortBy == null) {
            sortBy = ReviewSortBy.CREATED_AT;
        }
        if (order == null) {
            order = Sort.Direction.DESC;
        }

        String fieldName;
        switch (sortBy) {
            case RATING:
                fieldName = "rating";
                break;
            case CREATED_AT:
            default:
                fieldName = "createdAt";
                break;
        }

        return Sort.by(order, fieldName);
    }

    @Override
    public ReviewResponse update(String userId, String reviewId, UpdateReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Reseña no encontrada"));
        
        // Verificar que el usuario sea el dueño de la review
        if (!review.getUserId().equals(userId)) {
            throw new BadRequestException("No podés editar una reseña que no es tuya");
        }
        
        // Guardar el rating anterior para recalcular
        int oldRating = review.getRating();
        
        // Actualizar los campos
        review.setRating(request.rating());
        review.setComment(request.comment());
        
        Review updated = reviewRepository.save(review);
        
        // Recalcular el rating del profesional solo si cambió la calificación
        if (oldRating != request.rating()) {
            ProfessionalProfile profile = professionalRepository.findById(review.getProfessionalId())
                    .orElse(null);
            if (profile != null) {
                recalculateProfessionalRating(profile);
            }
        }
        
        return reviewMapper.toResponse(updated);
    }

    @Override
    public void delete(String userId, String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Reseña no encontrada"));
        
        // Verificar que el usuario sea el dueño de la review
        if (!review.getUserId().equals(userId)) {
            throw new BadRequestException("No podés eliminar una reseña que no es tuya");
        }
        
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

    @Override
    @Transactional(readOnly = true)
    public Optional<ReviewResponse> findUserReviewForProfessional(String userId, String professionalId) {
        return reviewRepository.findByUserIdAndProfessionalId(userId, professionalId)
                .map(reviewMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserReviewedProfessional(String userId, String professionalId) {
        return reviewRepository.existsByUserIdAndProfessionalId(userId, professionalId);
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
