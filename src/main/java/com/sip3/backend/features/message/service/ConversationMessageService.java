package com.sip3.backend.features.message.service;

import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.features.message.dto.CreateMessageRequest;
import com.sip3.backend.features.message.dto.MessageResponse;

public interface ConversationMessageService {

    MessageResponse sendMessage(String serviceOrderId, CreateMessageRequest request);

    PagedResponse<MessageResponse> listMessages(String serviceOrderId, int page, int size);
}
