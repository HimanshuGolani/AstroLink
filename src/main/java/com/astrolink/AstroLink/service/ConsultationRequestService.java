package com.astrolink.AstroLink.service;

import com.astrolink.AstroLink.dto.request.ConsultationRequestCreateDto;
import com.astrolink.AstroLink.dto.response.ConsultationResponseDto;
import com.astrolink.AstroLink.entity.ConsultationRequest;

import java.util.List;
import java.util.UUID;

public interface ConsultationRequestService {
    // Create a consultation request
    ConsultationResponseDto createConsultationRequest(UUID userId, ConsultationRequestCreateDto requestDto);

    // Astrologer accepting that request
    ConsultationResponseDto acceptConsultationRequest(UUID requestId, UUID astrologerId);

    // Get all available requests
    List<ConsultationResponseDto> getAllAvailableRequests(UUID astrologerId);

    // Get a user's available requests
    List<ConsultationResponseDto> getUserRequests(UUID userId);

    // Close request
    void closeRequest(UUID requestId);

    List<ConsultationResponseDto> findAllWaitingRequests(UUID userId);

}