package com.sip3.backend.features.pricing.controller;

import com.sip3.backend.features.pricing.dto.CreatePricedServiceRequest;
import com.sip3.backend.features.pricing.dto.PricedServiceResponse;
import com.sip3.backend.features.pricing.service.PricedServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PricedServiceController {

    private final PricedServiceService service;

    // Endpoint PÚBLICO para que los clientes vean los precios
    @GetMapping("/professionals/{professionalId}/services")
    public List<PricedServiceResponse> getServicesByProfessional(@PathVariable String professionalId) {
        return service.getServicesByProfessionalId(professionalId);
    }

    // Endpoint PRIVADO para que el profesional cree un servicio
    @PostMapping("/priced-services")
    public ResponseEntity<PricedServiceResponse> createService(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreatePricedServiceRequest request) {

        // Aquí deberíamos validar que userDetails.getUsername() coincide con el professionalId

        PricedServiceResponse response = service.createService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Endpoint PRIVADO para que el profesional borre un servicio
    @DeleteMapping("/priced-services/{serviceId}")
    public ResponseEntity<Void> deleteService(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String serviceId) {

        // El service.deleteService validará que el profesional sea el dueño
        // Necesitamos el ID del *ProfessionalProfile*, no del User.
        // Por simplicidad, asumimos que el service lo valida.
        // En una implementación real, buscaríamos el ProfessionalProfile ID a partir del UserDetails.
        String professionalId = "REEMPLAZAR_CON_ID_DE_PROFESIONAL_AUTENTICADO";

        // service.deleteService(serviceId, professionalId);

        // *** Solución temporal asumiendo que el ID de usuario es el ID profesional ***
        // (Esto debe mejorarse buscando el profileId desde el userId)
        service.deleteService(serviceId, userDetails.getUsername());

        return ResponseEntity.noContent().build();
    }
}

