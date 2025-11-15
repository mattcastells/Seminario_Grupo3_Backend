package com.sip3.backend.features.pricing.service;

import com.sip3.backend.common.exception.BadRequestException;
import com.sip3.backend.common.exception.NotFoundException;
import com.sip3.backend.features.pricing.dto.CreatePricedServiceRequest;
import com.sip3.backend.features.pricing.dto.PricedServiceResponse;
import com.sip3.backend.features.pricing.model.PricedService;
import com.sip3.backend.features.pricing.repository.PricedServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PricedServiceServiceImpl implements PricedServiceService {

    private final PricedServiceRepository repository;
    private final PricedServiceMapper mapper;

    @Override
    public List<PricedServiceResponse> getServicesByProfessionalId(String professionalId) {
        return repository.findByProfessionalId(professionalId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PricedServiceResponse createService(CreatePricedServiceRequest request) {
        PricedService entity = mapper.toEntity(request);
        // Aquí podríamos autocompletar el "trade" basado en el perfil del profesional
        PricedService saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    public void deleteService(String serviceId, String professionalId) {
        PricedService service = repository.findById(serviceId)
                .orElseThrow(() -> new NotFoundException("Servicio no encontrado"));

        if (!service.getProfessionalId().equals(professionalId)) {
            throw new BadRequestException("No tienes permiso para borrar este servicio");
        }

        repository.delete(service);
    }
}