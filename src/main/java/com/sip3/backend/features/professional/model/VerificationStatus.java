package com.sip3.backend.features.professional.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationStatus {
    private boolean faceVerified;
    private boolean dniFrontVerified;
    private boolean dniBackVerified;
}
