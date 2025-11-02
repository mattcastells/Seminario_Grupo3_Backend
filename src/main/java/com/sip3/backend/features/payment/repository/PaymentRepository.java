package com.sip3.backend.features.payment.repository;

import com.sip3.backend.features.payment.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String> {

    Page<Payment> findByUserId(String userId, Pageable pageable);

    Page<Payment> findByServiceOrderId(String serviceOrderId, Pageable pageable);
}
