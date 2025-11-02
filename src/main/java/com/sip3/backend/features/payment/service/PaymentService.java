package com.sip3.backend.features.payment.service;

import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.features.payment.dto.CreatePaymentRequest;
import com.sip3.backend.features.payment.dto.PaymentResponse;
import com.sip3.backend.features.payment.dto.UpdatePaymentStatusRequest;

public interface PaymentService {

    PaymentResponse create(String userId, CreatePaymentRequest request);

    PaymentResponse getById(String id);

    PagedResponse<PaymentResponse> listForUser(String userId, int page, int size);

    PagedResponse<PaymentResponse> listForServiceOrder(String serviceOrderId, int page, int size);

    PaymentResponse updateStatus(String id, UpdatePaymentStatusRequest request);

    void delete(String id);
}
