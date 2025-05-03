package com.astrolink.AstroLink.service;

import com.astrolink.AstroLink.dto.response.ChatInitiationResponse;
import com.astrolink.AstroLink.dto.response.ChatSessionDto;

import java.util.UUID;

public interface ChatService {
    ChatInitiationResponse createChat(UUID astrologerId, UUID consultationRequestId);
    }
