package com.sip3.backend.features.message.service;

import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.features.message.dto.CreateMessageRequest;
import com.sip3.backend.features.message.dto.MessageResponse;
import com.sip3.backend.features.message.model.ConversationMessage;
import com.sip3.backend.features.message.repository.ConversationMessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
public class ConversationMessageServiceImpl implements ConversationMessageService {

    private final ConversationMessageRepository conversationMessageRepository;
    private final ConversationMessageMapper mapper;

    public ConversationMessageServiceImpl(ConversationMessageRepository conversationMessageRepository,
                                          ConversationMessageMapper mapper) {
        this.conversationMessageRepository = conversationMessageRepository;
        this.mapper = mapper;
    }

    @Override
    public MessageResponse sendMessage(String serviceOrderId, CreateMessageRequest request) {
        ConversationMessage entity = mapper.toEntity(serviceOrderId, request);
        ConversationMessage saved = conversationMessageRepository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<MessageResponse> listMessages(String serviceOrderId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ConversationMessage> result = conversationMessageRepository
                .findByServiceOrderIdOrderByCreatedAtAsc(serviceOrderId, pageable);
        return new PagedResponse<>(
                result.getContent().stream().map(mapper::toResponse).collect(Collectors.toList()),
                result.getTotalElements(),
                result.getTotalPages(),
                result.getNumber(),
                result.getSize()
        );
    }
}
