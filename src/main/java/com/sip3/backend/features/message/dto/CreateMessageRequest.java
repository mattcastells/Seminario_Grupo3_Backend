package com.sip3.backend.features.message.dto;

import com.sip3.backend.features.message.model.MessageSenderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMessageRequest(
        @NotNull
        MessageSenderType senderType,

        @NotBlank
        String senderId,

        @NotBlank
        String content,

        String attachmentUrl
) {
}
