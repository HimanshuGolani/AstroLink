package com.astrolink.AstroLink.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatInitiationResponse {
    private PaymentStatusResponseDto paymentStatus;
    private ChatSessionDto chatSession;

}
