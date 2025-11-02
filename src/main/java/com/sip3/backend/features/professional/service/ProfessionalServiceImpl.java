package com.sip3.backend.features.professional.service;

import com.sip3.backend.common.exception.NotFoundException;
import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.features.professional.dto.CreateProfessionalRequest;
import com.sip3.backend.features.professional.dto.ProfessionalResponse;
import com.sip3.backend.features.professional.dto.UpdateProfessionalRequest;
import com.sip3.backend.features.professional.model.ProfessionalProfile;
import com.sip3.backend.features.professional.repository.ProfessionalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProfessionalServiceImpl implements ProfessionalService {

    private final ProfessionalRepository professionalRepository;
    private final ProfessionalMapper professionalMapper;
    private final MongoTemplate mongoTemplate;

    public ProfessionalServiceImpl(ProfessionalRepository professionalRepository,
                                   ProfessionalMapper professionalMapper,
                                   MongoTemplate mongoTemplate) {
        this.professionalRepository = professionalRepository;
        this.professionalMapper = professionalMapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ProfessionalResponse create(CreateProfessionalRequest request) {
        ProfessionalProfile entity = professionalMapper.toEntity(request);
        ProfessionalProfile saved = professionalRepository.save(entity);
        return professionalMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfessionalResponse getById(String id) {
        ProfessionalProfile profile = professionalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Profesional no encontrado"));
        return professionalMapper.toResponse(profile);
    }

    @Override
    public ProfessionalResponse update(String id, UpdateProfessionalRequest request) {
        ProfessionalProfile profile = professionalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Profesional no encontrado"));
        professionalMapper.updateEntity(profile, request);
        ProfessionalProfile saved = professionalRepository.save(profile);
        return professionalMapper.toResponse(saved);
    }

    @Override
    public void delete(String id) {
        if (!professionalRepository.existsById(id)) {
            throw new NotFoundException("Profesional no encontrado");
        }
        professionalRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ProfessionalResponse> search(String search, String profession, String tag, Double maxDistance,
                                                      Integer minExperience, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where("active").is(true));

        if (search != null && !search.isBlank()) {
            criteriaList.add(new Criteria().orOperator(
                    Criteria.where("displayName").regex(search, "i"),
                    Criteria.where("profession").regex(search, "i"),
                    Criteria.where("summary").regex(search, "i")
            ));
        }
        if (profession != null && !profession.isBlank()) {
            criteriaList.add(Criteria.where("profession").regex(profession, "i"));
        }
        if (tag != null && !tag.isBlank()) {
            criteriaList.add(Criteria.where("tags").in(tag));
        }
        if (maxDistance != null) {
            criteriaList.add(Criteria.where("distanceKm").lte(maxDistance));
        }
        if (minExperience != null) {
            criteriaList.add(Criteria.where("experienceYears").gte(minExperience));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList));
        }
        long total = mongoTemplate.count(query, ProfessionalProfile.class);
        query.with(pageable);
        List<ProfessionalProfile> profiles = mongoTemplate.find(query, ProfessionalProfile.class);
        Page<ProfessionalProfile> result = new PageImpl<>(profiles, pageable, total);

        List<ProfessionalResponse> content = result.getContent()
                .stream()
                .map(professionalMapper::toResponse)
                .collect(Collectors.toList());

        return new PagedResponse<>(content, total, result.getTotalPages(), result.getNumber(), result.getSize());
    }

    @Override
    @Transactional(readOnly = true)
    public ProfessionalResponse getByUserId(String userId) {
        ProfessionalProfile profile = professionalRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Profesional no encontrado para el usuario"));
        return professionalMapper.toResponse(profile);
    }
}
