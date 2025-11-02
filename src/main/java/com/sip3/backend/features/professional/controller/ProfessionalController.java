package com.sip3.backend.features.professional.controller;

import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.features.professional.dto.CreateProfessionalRequest;
import com.sip3.backend.features.professional.dto.ProfessionalResponse;
import com.sip3.backend.features.professional.dto.UpdateProfessionalRequest;
import com.sip3.backend.features.professional.service.ProfessionalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/v1/professionals")
public class ProfessionalController {

    private final ProfessionalService professionalService;

    public ProfessionalController(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSIONAL')")
    public ResponseEntity<ProfessionalResponse> create(@Valid @RequestBody CreateProfessionalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(professionalService.create(request));
    }

    @GetMapping
    public PagedResponse<ProfessionalResponse> search(@RequestParam(required = false) String search,
                                                      @RequestParam(required = false) String profession,
                                                      @RequestParam(required = false) String tag,
                                                      @RequestParam(required = false) Double maxDistance,
                                                      @RequestParam(required = false) Integer minExperience,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "20") int size) {
        return professionalService.search(search, profession, tag, maxDistance, minExperience, page, size);
    }

    @GetMapping("/{id}")
    public ProfessionalResponse getById(@PathVariable String id) {
        return professionalService.getById(id);
    }

    @GetMapping("/by-user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSIONAL','USER')")
    public ProfessionalResponse getByUserId(@PathVariable String userId) {
        return professionalService.getByUserId(userId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROFESSIONAL')")
    public ProfessionalResponse update(@PathVariable String id,
                                       @Valid @RequestBody UpdateProfessionalRequest request) {
        return professionalService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        professionalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
