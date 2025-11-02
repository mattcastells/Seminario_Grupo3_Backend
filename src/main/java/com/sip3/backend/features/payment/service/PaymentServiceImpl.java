package com.sip3.backend.features.payment.service;

import com.sip3.backend.common.exception.NotFoundException;
import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.features.payment.dto.CreatePaymentRequest;
import com.sip3.backend.features.payment.dto.PaymentResponse;
import com.sip3.backend.features.payment.dto.UpdatePaymentStatusRequest;
import com.sip3.backend.features.payment.model.Payment;
import com.sip3.backend.features.payment.model.PaymentStatus;
import com.sip3.backend.features.payment.repository.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public PaymentResponse create(String userId, CreatePaymentRequest request) {
        Payment payment = paymentMapper.toEntity(userId, request);
        Payment saved = paymentRepository.save(payment);
        // simulate processing and mark as successful immediately for demo purposes
        saved.setProviderReference(UUID.randomUUID().toString());
        paymentMapper.markProcessed(saved);
        saved = paymentRepository.save(saved);
        return paymentMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getById(String id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pago no encontrado"));
        return paymentMapper.toResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<PaymentResponse> listForUser(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Payment> result = paymentRepository.findByUserId(userId, pageable);
        return new PagedResponse<>(
                result.getContent().stream().map(paymentMapper::toResponse).collect(Collectors.toList()),
                result.getTotalElements(),
                result.getTotalPages(),
                result.getNumber(),
                result.getSize()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<PaymentResponse> listForServiceOrder(String serviceOrderId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Payment> result = paymentRepository.findByServiceOrderId(serviceOrderId, pageable);
        return new PagedResponse<>(
                result.getContent().stream().map(paymentMapper::toResponse).collect(Collectors.toList()),
                result.getTotalElements(),
                result.getTotalPages(),
                result.getNumber(),
                result.getSize()
        );
    }

    @Override
    public PaymentResponse updateStatus(String id, UpdatePaymentStatusRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pago no encontrado"));
        if (request.status() != null) {
            payment.setStatus(request.status());
            if (request.status() == PaymentStatus.SUCCESS) {
                payment.setProcessedAt(Instant.now());
            }
        }
        if (request.providerReference() != null) {
            payment.setProviderReference(request.providerReference());
        }
        if (request.notes() != null) {
            payment.setNotes(request.notes());
        }
        Payment saved = paymentRepository.save(payment);
        return paymentMapper.toResponse(saved);
    }

    @Override
    public void delete(String id) {
        if (!paymentRepository.existsById(id)) {
            throw new NotFoundException("Pago no encontrado");
        }
        paymentRepository.deleteById(id);
    }
}
