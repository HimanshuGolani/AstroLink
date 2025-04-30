package com.astrolink.AstroLink.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentStatusResponseDto {
    private String status;
    private String message;
    private String sessionId;
    private String sessionUrl;
}
