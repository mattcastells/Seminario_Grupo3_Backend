package com.sip3.backend.features.serviceorder.service;

import com.sip3.backend.common.exception.NotFoundException;
import com.sip3.backend.common.payload.PagedResponse;
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
import java.util.stream.Collectors;

@Service
@Transactional
public class ServiceOrderServiceImpl implements ServiceOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceOrderMapper serviceOrderMapper;

    public ServiceOrderServiceImpl(ServiceOrderRepository serviceOrderRepository,
                                   ServiceOrderMapper serviceOrderMapper) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.serviceOrderMapper = serviceOrderMapper;
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
}
