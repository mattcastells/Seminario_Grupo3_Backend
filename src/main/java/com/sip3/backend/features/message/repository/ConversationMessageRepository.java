package com.sip3.backend.features.message.repository;

import com.sip3.backend.features.message.model.ConversationMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConversationMessageRepository extends MongoRepository<ConversationMessage, String> {

    Page<ConversationMessage> findByServiceOrderIdOrderByCreatedAtAsc(String serviceOrderId, Pageable pageable);
}
