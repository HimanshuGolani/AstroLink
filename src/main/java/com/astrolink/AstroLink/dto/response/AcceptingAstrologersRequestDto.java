package com.astrolink.AstroLink.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcceptingAstrologersRequestDto {
    private UUID userId;
    private UUID astrologerId;
    private int astrologerRating;
    private ConsultationResponseDto consultationRequest;
}
