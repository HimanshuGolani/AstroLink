package com.astrolink.AstroLink.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentVerificationRequestDto {
    private String sessionId;
    private UUID userId;
}
