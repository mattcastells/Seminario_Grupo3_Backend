package com.sip3.backend.features.serviceorder.service;

import com.sip3.backend.common.exception.NotFoundException;
import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.features.professional.model.ProfessionalProfile;
import com.sip3.backend.features.professional.repository.ProfessionalRepository;
import com.sip3.backend.features.review.model.Review;
import com.sip3.backend.features.review.repository.ReviewRepository;
import com.sip3.backend.features.serviceorder.dto.CompleteServiceOrderRequest;
import com.sip3.backend.features.serviceorder.dto.CreateServiceOrderRequest;
import com.sip3.backend.features.serviceorder.dto.ServiceOrderResponse;
import com.sip3.backend.features.serviceorder.dto.UpdateServiceOrderRequest;
import com.sip3.backend.features.serviceorder.model.ServiceOrder;
import com.sip3.backend.features.serviceorder.model.ServiceOrderStatus;
import com.sip3.backend.features.serviceorder.repository.ServiceOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServiceOrderServiceImpl implements ServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceOrderMapper serviceOrderMapper;
    private final ProfessionalRepository professionalRepository;
    private final ReviewRepository reviewRepository;

    public ServiceOrderServiceImpl(ServiceOrderRepository serviceOrderRepository,
                                   ServiceOrderMapper serviceOrderMapper,
                                   ProfessionalRepository professionalRepository,
                                   ReviewRepository reviewRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.serviceOrderMapper = serviceOrderMapper;
        this.professionalRepository = professionalRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public ServiceOrderResponse create(String userId, CreateServiceOrderRequest request) {
        ServiceOrder order = serviceOrderMapper.toEntity(userId, request);
        ServiceOrder saved = serviceOrderRepository.save(order);
        return serviceOrderMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceOrderResponse getById(String id) {
        ServiceOrder order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"));
        return serviceOrderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ServiceOrderResponse> listForUser(String userId, ServiceOrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ServiceOrder> result;
        if (status != null) {
            result = serviceOrderRepository.findByUserIdAndStatus(userId, status, pageable);
        } else {
            result = serviceOrderRepository.findByUserId(userId, pageable);
        }
        return new PagedResponse<>(
                result.getContent().stream().map(serviceOrderMapper::toResponse).collect(Collectors.toList()),
                result.getTotalElements(),
                result.getTotalPages(),
                result.getNumber(),
                result.getSize()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ServiceOrderResponse> listForProfessional(String professionalId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ServiceOrder> result = serviceOrderRepository.findByProfessionalId(professionalId, pageable);
        return new PagedResponse<>(
                result.getContent().stream().map(serviceOrderMapper::toResponse).collect(Collectors.toList()),
                result.getTotalElements(),
                result.getTotalPages(),
                result.getNumber(),
                result.getSize()
        );
    }

    @Override
    public ServiceOrderResponse update(String id, UpdateServiceOrderRequest request) {
        ServiceOrder order = serviceOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"));
        serviceOrderMapper.updateEntity(order, request);
        if (request.status() == ServiceOrderStatus.COMPLETED && order.getLastMessageAt() == null) {
            order.setLastMessageAt(Instant.now());
        }
        ServiceOrder saved = serviceOrderRepository.save(order);
        return serviceOrderMapper.toResponse(saved);
    }

    @Override
    public void delete(String id) {
        if (!serviceOrderRepository.existsById(id)) {
            throw new NotFoundException("Solicitud no encontrada");
        }
        serviceOrderRepository.deleteById(id);
    }

    @Override
    public ServiceOrderResponse complete(String userId, String serviceOrderId, CompleteServiceOrderRequest request) {
        // Obtener la orden de servicio
        ServiceOrder order = serviceOrderRepository.findById(serviceOrderId)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"));

        // Verificar que el usuario sea el dueño de la orden
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("No tenés permiso para completar esta orden");
        }

        // Verificar que la orden no esté ya completada
        if (order.getStatus() == ServiceOrderStatus.COMPLETED) {
            throw new RuntimeException("Esta orden ya fue completada");
        }

        // Actualizar el estado de la orden
        order.setStatus(ServiceOrderStatus.COMPLETED);
        order.setLastMessageAt(Instant.now());
        ServiceOrder savedOrder = serviceOrderRepository.save(order);

        // Crear la review
        Review review = Review.builder()
                .professionalId(order.getProfessionalId())
                .userId(userId)
                .rating(request.rating())
                .comment(request.comment())
                .createdAt(Instant.now())
                .build();
        reviewRepository.save(review);

        // Actualizar el rating del profesional
        ProfessionalProfile professional = professionalRepository.findById(order.getProfessionalId())
                .orElseThrow(() -> new NotFoundException("Profesional no encontrado"));

        // Obtener todas las reviews del profesional
        List<Review> allReviews = reviewRepository.findByProfessionalId(order.getProfessionalId());
        
        // Calcular el nuevo rating promedio
        double newRating = allReviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        professional.setRating(newRating);
        professional.setReviewsCount(allReviews.size());
        professionalRepository.save(professional);

        return serviceOrderMapper.toResponse(savedOrder);
    }
}
