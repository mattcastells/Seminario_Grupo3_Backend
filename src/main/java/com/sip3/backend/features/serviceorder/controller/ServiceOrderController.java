package com.sip3.backend.features.serviceorder.controller;

import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.features.serviceorder.dto.CompleteServiceOrderRequest;
import com.sip3.backend.features.serviceorder.dto.CreateServiceOrderRequest;
import com.sip3.backend.features.serviceorder.dto.ServiceOrderResponse;
import com.sip3.backend.features.serviceorder.dto.UpdateServiceOrderRequest;
import com.sip3.backend.features.serviceorder.model.ServiceOrderStatus;
import com.sip3.backend.features.serviceorder.service.ServiceOrderService;
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
@RequestMapping("/api/v1/service-orders")
public class ServiceOrderController {

    private final ServiceOrderService serviceOrderService;

    public ServiceOrderController(ServiceOrderService serviceOrderService) {
        this.serviceOrderService = serviceOrderService;
    }

    @PostMapping
    public ResponseEntity<ServiceOrderResponse> create(@AuthenticationPrincipal UserDetails userDetails,
                                                       @Valid @RequestBody CreateServiceOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceOrderService.create(userDetails.getUsername(), request));
    }

    @GetMapping("/{id}")
    public ServiceOrderResponse getById(@PathVariable String id) {
        return serviceOrderService.getById(id);
    }

    @GetMapping("/me")
    public PagedResponse<ServiceOrderResponse> listForCurrentUser(@AuthenticationPrincipal UserDetails userDetails,
                                                                  @RequestParam(required = false) ServiceOrderStatus status,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "20") int size) {
        return serviceOrderService.listForUser(userDetails.getUsername(), status, page, size);
    }

    @GetMapping("/professional/{professionalId}")
    public PagedResponse<ServiceOrderResponse> listForProfessional(@PathVariable String professionalId,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "20") int size) {
        return serviceOrderService.listForProfessional(professionalId, page, size);
    }

    @PutMapping("/{id}")
    public ServiceOrderResponse update(@PathVariable String id,
                                       @Valid @RequestBody UpdateServiceOrderRequest request) {
        return serviceOrderService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        serviceOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/complete")
    public ServiceOrderResponse complete(@AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable String id,
                                         @Valid @RequestBody CompleteServiceOrderRequest request) {
        return serviceOrderService.complete(userDetails.getUsername(), id, request);
    }
}
