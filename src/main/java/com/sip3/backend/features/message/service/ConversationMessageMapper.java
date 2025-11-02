package com.sip3.backend.features.message.service;

import com.sip3.backend.features.message.dto.CreateMessageRequest;
import com.sip3.backend.features.message.dto.MessageResponse;
import com.sip3.backend.features.message.model.ConversationMessage;
import org.springframework.stereotype.Component;

@Component
public class ConversationMessageMapper {

    public ConversationMessage toEntity(String serviceOrderId, CreateMessageRequest request) {
        return ConversationMessage.builder()
                .serviceOrderId(serviceOrderId)
                .senderType(request.senderType())
                .senderId(request.senderId())
                .content(request.content())
                .attachmentUrl(request.attachmentUrl())
                .build();
    }

    public MessageResponse toResponse(ConversationMessage message) {
        return new MessageResponse(
                message.getId(),
                message.getServiceOrderId(),
                message.getSenderType(),
                message.getSenderId(),
                message.getContent(),
                message.getAttachmentUrl(),
                message.getCreatedAt()
        );
    }
}
