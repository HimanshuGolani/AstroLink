package com.astrolink.AstroLink.service;

import com.astrolink.AstroLink.dto.response.ChatMessageDto;
import com.astrolink.AstroLink.dto.response.ChatSessionDto;
import com.astrolink.AstroLink.entity.MessageType;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    ChatSessionDto createChatSession(UUID consultationRequestId, UUID astrologerId);
    void deleteChatSession(UUID consultationRequestId);
    ChatMessageDto saveMessage(UUID chatSessionId, UUID senderId, String content, String imageUrl);
    ChatMessageDto saveMessage(UUID chatSessionId, UUID senderId, String content, String imageUrl, MessageType type);
    List<ChatMessageDto> getMessages(UUID chatSessionId);
}