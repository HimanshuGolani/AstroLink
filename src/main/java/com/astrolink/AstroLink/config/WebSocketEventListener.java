package com.astrolink.AstroLink.config;

import com.astrolink.AstroLink.dto.response.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        UUID userId = (UUID) headerAccessor.getSessionAttributes().get("userId");
        UUID chatSessionId = (UUID) headerAccessor.getSessionAttributes().get("chatSessionId");

        if (userId != null && chatSessionId != null) {
            log.info("User disconnected: {} from chat session: {}", userId, chatSessionId);

            ChatMessageDto chatMessage = ChatMessageDto.builder()
                    .id(UUID.randomUUID())
                    .senderId(userId)
                    .chatSessionId(chatSessionId)
                    .content("User has left the chat")
                    .timestamp(LocalDateTime.now())
                    .build();

            messagingTemplate.convertAndSend("/topic/chat/" + chatSessionId, chatMessage);
        }
    }
}