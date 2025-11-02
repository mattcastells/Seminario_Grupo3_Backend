package com.sip3.backend.features.message.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
public class ConversationMessage {

    @Id
    private String id;

    private String serviceOrderId;

    private MessageSenderType senderType;

    private String senderId;

    private String content;

    private String attachmentUrl;

    @CreatedDate
    private Instant createdAt;
}
