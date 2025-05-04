package com.astrolink.AstroLink.service;

import com.astrolink.AstroLink.dto.response.ChatDto;
import com.astrolink.AstroLink.dto.response.ChatInitiationResponse;
import com.astrolink.AstroLink.dto.response.ChatSessionDto;
import com.astrolink.AstroLink.dto.response.SmallChatsResponseDto;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    ChatInitiationResponse createChat(UUID astrologerId, UUID consultationRequestId);
    List<SmallChatsResponseDto> getAllSmallChats(UUID userId);
    ChatDto getChatById(UUID userId, UUID chatId);
}
