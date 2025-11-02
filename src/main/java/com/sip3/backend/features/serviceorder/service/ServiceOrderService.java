package com.sip3.backend.features.serviceorder.service;

import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.features.serviceorder.dto.CreateServiceOrderRequest;
import com.sip3.backend.features.serviceorder.dto.ServiceOrderResponse;
import com.sip3.backend.features.serviceorder.dto.UpdateServiceOrderRequest;
import com.sip3.backend.features.serviceorder.model.ServiceOrderStatus;

public interface ServiceOrderService {

    ServiceOrderResponse create(String userId, CreateServiceOrderRequest request);

    ServiceOrderResponse getById(String id);

    PagedResponse<ServiceOrderResponse> listForUser(String userId, ServiceOrderStatus status, int page, int size);

    PagedResponse<ServiceOrderResponse> listForProfessional(String professionalId, int page, int size);

    ServiceOrderResponse update(String id, UpdateServiceOrderRequest request);

    void delete(String id);
}
