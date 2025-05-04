package com.astrolink.AstroLink.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDto {
    private List<ConsultationResponseDto> recentConsultations;
    private int activeChatsCount;
}
