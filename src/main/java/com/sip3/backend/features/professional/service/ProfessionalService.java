package com.sip3.backend.features.professional.service;

import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.features.professional.dto.CreateProfessionalRequest;
import com.sip3.backend.features.professional.dto.ProfessionalResponse;
import com.sip3.backend.features.professional.dto.UpdateProfessionalRequest;

public interface ProfessionalService {

    ProfessionalResponse create(CreateProfessionalRequest request);

    ProfessionalResponse getById(String id);

    ProfessionalResponse update(String id, UpdateProfessionalRequest request);

    void delete(String id);

    PagedResponse<ProfessionalResponse> search(String search, String profession, String tag, Double maxDistance,
                                               Integer minExperience, int page, int size);

    ProfessionalResponse getByUserId(String userId);

    void recalculateAllReviewsCount();
}
