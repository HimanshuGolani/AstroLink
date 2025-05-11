package com.astrolink.AstroLink.controller;

import com.astrolink.AstroLink.dto.request.MessageDto;
import com.astrolink.AstroLink.dto.response.ChatMessageDto;
import com.astrolink.AstroLink.service.MessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MessagingController {

    private final MessagingService messagingService;

    @MessageMapping("/sendMessage/{chatId}")
    @SendTo("/topic/chat/{chatId}")
    public ChatMessageDto publishMessageInChat(
            @DestinationVariable UUID chatId,
            MessageDto message
    ) {
        return messagingService.publishMessage(chatId, message);
    }
}
