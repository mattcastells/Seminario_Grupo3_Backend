package com.sip3.backend.features.payment.controller;

import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.features.payment.dto.CreatePaymentRequest;
import com.sip3.backend.features.payment.dto.PaymentResponse;
import com.sip3.backend.features.payment.dto.UpdatePaymentStatusRequest;
import com.sip3.backend.features.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@AuthenticationPrincipal UserDetails userDetails,
                                                  @Valid @RequestBody CreatePaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.create(userDetails.getUsername(), request));
    }

    @GetMapping("/{id}")
    public PaymentResponse getById(@PathVariable String id) {
        return paymentService.getById(id);
    }

    @GetMapping
    public PagedResponse<PaymentResponse> listForUser(@AuthenticationPrincipal UserDetails userDetails,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "20") int size) {
        return paymentService.listForUser(userDetails.getUsername(), page, size);
    }

    @GetMapping("/service-order/{serviceOrderId}")
    public PagedResponse<PaymentResponse> listForServiceOrder(@PathVariable String serviceOrderId,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "20") int size) {
        return paymentService.listForServiceOrder(serviceOrderId, page, size);
    }

    @PutMapping("/{id}")
    public PaymentResponse updateStatus(@PathVariable String id,
                                        @Valid @RequestBody UpdatePaymentStatusRequest request) {
        return paymentService.updateStatus(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
