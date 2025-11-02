package com.sip3.backend.features.message.controller;

import com.sip3.backend.common.payload.PagedResponse;
import com.sip3.backend.features.message.dto.CreateMessageRequest;
import com.sip3.backend.features.message.dto.MessageResponse;
import com.sip3.backend.features.message.service.ConversationMessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service-orders/{serviceOrderId}/messages")
public class ConversationMessageController {

    private final ConversationMessageService conversationMessageService;

    public ConversationMessageController(ConversationMessageService conversationMessageService) {
        this.conversationMessageService = conversationMessageService;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> sendMessage(@PathVariable String serviceOrderId,
                                                       @Valid @RequestBody CreateMessageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(conversationMessageService.sendMessage(serviceOrderId, request));
    }

    @GetMapping
    public PagedResponse<MessageResponse> listMessages(@PathVariable String serviceOrderId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "50") int size) {
        return conversationMessageService.listMessages(serviceOrderId, page, size);
    }
}
