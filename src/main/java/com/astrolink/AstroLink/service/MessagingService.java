package com.astrolink.AstroLink.service;

import com.astrolink.AstroLink.dto.request.MessageDto;
import com.astrolink.AstroLink.dto.response.ChatMessageDto;

import java.util.UUID;

public interface MessagingService {
    ChatMessageDto publishMessage(UUID chatId , MessageDto message);
}
