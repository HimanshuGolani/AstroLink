package com.astrolink.AstroLink.dto.response;

import com.astrolink.AstroLink.entity.PaymentStatus;
import com.astrolink.AstroLink.entity.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationResponseDto {
    private UUID id;
    private String title;
    private String birthDate;
    private String birthTime;
    private String birthPlace;
    private PaymentStatus paymentStatus;
    private RequestStatus requestStatus;
    private boolean isOpenForAll;
    private LocalDateTime createdAt;
    private List<AstrologerDetailsDto> astrologerDetails;
    private int acceptingAstrologersCount;
}
