package com.sip3.backend.features.serviceorder.service;

import com.sip3.backend.features.serviceorder.dto.CreateServiceOrderRequest;
import com.sip3.backend.features.serviceorder.dto.ServiceOrderResponse;
import com.sip3.backend.features.serviceorder.dto.UpdateServiceOrderRequest;
import com.sip3.backend.features.serviceorder.model.ServiceOrder;
import com.sip3.backend.features.serviceorder.model.ServiceOrderStatus;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ServiceOrderMapper {

    public ServiceOrder toEntity(String userId, CreateServiceOrderRequest request) {
        return ServiceOrder.builder()
                .userId(userId)
                .professionalId(request.professionalId())
                .contactName(request.contactName())
                .contactPhone(request.contactPhone())
                .contactEmail(request.contactEmail())
                .address(request.address())
                .serviceType(request.serviceType())
                .description(request.description())
                .preferredDate(request.preferredDate())
                .budget(request.budget())
                .paymentPreference(request.paymentPreference())
                .status(ServiceOrderStatus.PENDING)
                .build();
    }

    public ServiceOrderResponse toResponse(ServiceOrder order) {
        return new ServiceOrderResponse(
                order.getId(),
                order.getUserId(),
                order.getProfessionalId(),
                order.getContactName(),
                order.getContactPhone(),
                order.getContactEmail(),
                order.getAddress(),
                order.getServiceType(),
                order.getDescription(),
                order.getPreferredDate(),
                order.getBudget(),
                order.getPaymentPreference(),
                order.getStatus(),
                order.getScheduledAt(),
                order.getLastMessagePreview(),
                order.getLastMessageAt(),
                order.getCompletedByClient(),
                order.getClientRating(),
                order.getClientComment(),
                order.getCompletedByProfessional(),
                order.getProfessionalRating(),
                order.getProfessionalComment(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    public void updateEntity(ServiceOrder order, UpdateServiceOrderRequest request) {
        if (request.contactName() != null) {
            order.setContactName(request.contactName());
        }
        if (request.contactPhone() != null) {
            order.setContactPhone(request.contactPhone());
        }
        if (request.contactEmail() != null) {
            order.setContactEmail(request.contactEmail());
        }
        if (request.address() != null) {
            order.setAddress(request.address());
        }
        if (request.serviceType() != null) {
            order.setServiceType(request.serviceType());
        }
        if (request.description() != null) {
            order.setDescription(request.description());
        }
        if (request.preferredDate() != null) {
            order.setPreferredDate(request.preferredDate());
        }
        if (request.budget() != null) {
            order.setBudget(request.budget());
        }
        if (request.paymentPreference() != null) {
            order.setPaymentPreference(request.paymentPreference());
        }
        if (request.status() != null) {
            order.setStatus(request.status());
        }
        if (request.scheduledAtEpoch() != null) {
            order.setScheduledAt(Instant.ofEpochMilli(request.scheduledAtEpoch()));
        }
        if (request.lastMessagePreview() != null) {
            order.setLastMessagePreview(request.lastMessagePreview());
            order.setLastMessageAt(Instant.now());
        }
    }
}
