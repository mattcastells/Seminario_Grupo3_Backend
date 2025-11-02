package com.sip3.backend.features.serviceorder.repository;

import com.sip3.backend.features.serviceorder.model.ServiceOrder;
import com.sip3.backend.features.serviceorder.model.ServiceOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServiceOrderRepository extends MongoRepository<ServiceOrder, String> {

    Page<ServiceOrder> findByUserId(String userId, Pageable pageable);

    Page<ServiceOrder> findByProfessionalId(String professionalId, Pageable pageable);

    Page<ServiceOrder> findByUserIdAndStatus(String userId, ServiceOrderStatus status, Pageable pageable);
}
