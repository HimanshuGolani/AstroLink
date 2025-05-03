package com.astrolink.AstroLink.service;

import com.astrolink.AstroLink.dto.request.ConsultationRequestCreateDto;
import com.astrolink.AstroLink.dto.response.ConsultationRequestDto;

import java.util.List;
import java.util.UUID;

public interface ConsultationRequestService {
    // Create a consultation request
    ConsultationRequestDto createConsultationRequest(UUID userId, ConsultationRequestCreateDto requestDto);

    // Astrologer accepting that request
    ConsultationRequestDto acceptConsultationRequest(UUID requestId, UUID astrologerId);

    // Get all available requests
    List<ConsultationRequestDto> getAllAvailableRequests();

    // Get a user's available requests
    List<ConsultationRequestDto> getUserRequests(UUID userId);

    // Close request
    void closeRequest(UUID requestId);
}