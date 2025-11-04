package com.sip3.backend.features.serviceorder.service;

import com.sip3.backend.common.exception.NotFoundException;
import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.features.professional.model.ProfessionalProfile;
import com.sip3.backend.features.professional.repository.ProfessionalRepository;
import com.sip3.backend.features.review.model.Review;
import com.sip3.backend.features.review.repository.ReviewRepository;
import com.sip3.backend.features.serviceorder.dto.CancelServiceOrderRequest;
import com.sip3.backend.features.serviceorder.dto.CompleteServiceOrderRequest;
import com.sip3.backend.features.serviceorder.dto.CreateServiceOrderRequest;
import com.sip3.backend.features.serviceorder.dto.ServiceOrderResponse;
import com.sip3.backend.features.serviceorder.dto.UpdateServiceOrderRequest;
import com.sip3.backend.features.serviceorder.model.ServiceOrder;
import com.sip3.backend.features.serviceorder.model.ServiceOrderStatus;
import com.sip3.backend.features.serviceorder.repository.ServiceOrderRepository;
import com.sip3.backend.user.repository.UserRepository;
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
    private final UserRepository userRepository;

    public ServiceOrderServiceImpl(ServiceOrderRepository serviceOrderRepository,
                                   ServiceOrderMapper serviceOrderMapper,
                                   ProfessionalRepository professionalRepository,
                                   ReviewRepository reviewRepository,
                                   UserRepository userRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.serviceOrderMapper = serviceOrderMapper;
        this.professionalRepository = professionalRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
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
            result = serviceOrderRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status, pageable);
        } else {
            result = serviceOrderRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
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
        Page<ServiceOrder> result = serviceOrderRepository.findByProfessionalIdOrderByCreatedAtDesc(professionalId, pageable);
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

        // Determinar si quien completa es el cliente o el profesional
        boolean isClient = order.getUserId().equals(userId);
        boolean isProfessional = false;

        // DEBUG LOGS
        System.out.println("=== COMPLETE ORDER DEBUG ===");
        System.out.println("userId recibido: '" + userId + "'");
        System.out.println("order.getUserId(): '" + order.getUserId() + "'");
        System.out.println("order.getProfessionalId(): '" + order.getProfessionalId() + "'");
        System.out.println("isClient: " + isClient);

        // Verificar si el usuario es el profesional de esta orden
        // El userId que llega es el username, y ProfessionalProfile.userId es el User._id (MongoDB ObjectId)
        // Necesitamos: username -> User._id -> comparar con ProfessionalProfile.userId
        try {
            // Paso 1: Buscar el User por username para obtener su _id
            var user = userRepository.findByUsernameIgnoreCase(userId).orElse(null);
            System.out.println("User encontrado por username '" + userId + "': " + (user != null));

            if (user != null) {
                String userMongoId = user.getId();
                System.out.println("User MongoDB ID: '" + userMongoId + "'");

                // Paso 2: Buscar el ProfessionalProfile por userId (que es el User._id)
                ProfessionalProfile professionalProfile = professionalRepository.findByUserId(userMongoId).orElse(null);
                System.out.println("ProfessionalProfile encontrado: " + (professionalProfile != null));

                if (professionalProfile != null) {
                    System.out.println("ProfessionalProfile.getId(): '" + professionalProfile.getId() + "'");
                    System.out.println("Order.getProfessionalId(): '" + order.getProfessionalId() + "'");

                    // Paso 3: Comparar el ID del profesional con el professionalId de la orden
                    if (professionalProfile.getId().equals(order.getProfessionalId())) {
                        isProfessional = true;
                        System.out.println("¡MATCH! Usuario ES el profesional de esta orden");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error verificando si es profesional: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("isProfessional FINAL: " + isProfessional);
        System.out.println("=== FIN DEBUG ===");

        // Verificar que el usuario tenga permiso (sea cliente o profesional de esta orden)
        if (!isClient && !isProfessional) {
            throw new RuntimeException("No tenés permiso para completar esta orden");
        }

        // Verificar que la orden no esté ya completada por completo
        if (order.getStatus() == ServiceOrderStatus.COMPLETED) {
            throw new RuntimeException("Esta orden ya fue completada por ambas partes");
        }

        // Actualizar según quien completa
        if (isClient) {
            // Cliente completa y califica al profesional
            if (Boolean.TRUE.equals(order.getCompletedByClient())) {
                throw new RuntimeException("Ya completaste y calificaste este trabajo");
            }

            order.setCompletedByClient(true);
            order.setClientRating(request.rating());
            order.setClientComment(request.comment());

            // Crear review para el profesional
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
            List<Review> allReviews = reviewRepository.findByProfessionalId(order.getProfessionalId());
            double newRating = allReviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            professional.setRating(newRating);
            professional.setReviewsCount(allReviews.size());
            professionalRepository.save(professional);

        } else if (isProfessional) {
            // Profesional completa y califica al cliente
            if (Boolean.TRUE.equals(order.getCompletedByProfessional())) {
                throw new RuntimeException("Ya completaste y calificaste este trabajo");
            }

            order.setCompletedByProfessional(true);
            order.setProfessionalRating(request.rating());
            order.setProfessionalComment(request.comment());

            // TODO: Crear review para el cliente (si implementamos sistema de reviews para clientes)
            // Por ahora solo guardamos en la orden
        }

        // Solo marcar como COMPLETED si ambos completaron
        if (Boolean.TRUE.equals(order.getCompletedByClient()) &&
            Boolean.TRUE.equals(order.getCompletedByProfessional())) {
            order.setStatus(ServiceOrderStatus.COMPLETED);
        }

        order.setLastMessageAt(Instant.now());
        ServiceOrder savedOrder = serviceOrderRepository.save(order);

        return serviceOrderMapper.toResponse(savedOrder);
    }

    @Override
    public ServiceOrderResponse cancel(String userId, String serviceOrderId, CancelServiceOrderRequest request) {
        // Obtener la orden de servicio
        ServiceOrder order = serviceOrderRepository.findById(serviceOrderId)
                .orElseThrow(() -> new NotFoundException("Solicitud no encontrada"));

        // Verificar que el usuario sea el dueño de la orden
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("No tenés permiso para cancelar esta orden");
        }

        // Verificar que la orden no esté ya completada o cancelada
        if (order.getStatus() == ServiceOrderStatus.COMPLETED) {
            throw new RuntimeException("No se puede cancelar un trabajo ya completado");
        }
        if (order.getStatus() == ServiceOrderStatus.CANCELLED) {
            throw new RuntimeException("Esta orden ya fue cancelada");
        }

        // Actualizar el estado de la orden a CANCELLED
        order.setStatus(ServiceOrderStatus.CANCELLED);
        order.setLastMessageAt(Instant.now());
        ServiceOrder savedOrder = serviceOrderRepository.save(order);

        return serviceOrderMapper.toResponse(savedOrder);
    }
}
