package com.astrolink.AstroLink.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationRequestCreateDto {
    @NotNull(message = "The title cannot be null")
    private String title;
    private String birthDate;
    private String birthTime;
    private String birthPlace;
}
