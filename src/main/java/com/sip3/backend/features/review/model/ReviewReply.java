package com.sip3.backend.features.review.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "review_replies")
public class ReviewReply {

    @Id
    private String id;

    private String reviewId;

    private String professionalId;

    private String professionalName;

    private String reply;

    private Instant createdAt;

    private Instant updatedAt;
}

