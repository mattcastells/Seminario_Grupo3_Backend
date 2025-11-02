package com.sip3.backend.features.message.dto;

import com.sip3.backend.features.message.model.MessageSenderType;

import java.time.Instant;

public record MessageResponse(
        String id,
        String serviceOrderId,
        MessageSenderType senderType,
        String senderId,
        String content,
        String attachmentUrl,
        Instant createdAt
) {
}
