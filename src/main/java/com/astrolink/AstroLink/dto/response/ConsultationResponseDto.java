package com.astrolink.AstroLink.dto.response;

import com.astrolink.AstroLink.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private boolean isOpenForAll;
    private LocalDateTime createdAt;
    private int acceptingAstrologersCount;
}