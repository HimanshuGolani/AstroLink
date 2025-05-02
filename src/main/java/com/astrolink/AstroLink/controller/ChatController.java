package com.astrolink.AstroLink.controller;

import com.astrolink.AstroLink.dto.response.ChatMessageDto;
import com.astrolink.AstroLink.dto.response.ChatSessionDto;
import com.astrolink.AstroLink.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * WebSocket endpoint to send messages to public topic
     */
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDto sendPublicMessage(@Payload ChatMessageDto chatMessageDto) {
        return chatService.saveMessage(
                chatMessageDto.getChatSessionId(),
                chatMessageDto.getSenderId(),
                chatMessageDto.getContent(),
                chatMessageDto.getImageUrl()
        );
    }

    /**
     * WebSocket endpoint to send messages to a specific chat session
     */
    @MessageMapping("/chat.sendMessage/{chatSessionId}")
    @SendTo("/topic/chat/{chatSessionId}")
    public ChatMessageDto sendSessionMessage(
            @DestinationVariable UUID chatSessionId,
            @Payload ChatMessageDto chatMessageDto) {
        return chatService.saveMessage(
                chatSessionId,
                chatMessageDto.getSenderId(),
                chatMessageDto.getContent(),
                chatMessageDto.getImageUrl()
        );
    }

    /**
     * WebSocket endpoint to send private messages to a specific user
     */
    @MessageMapping("/chat.private/{recipientId}")
    public void sendPrivateMessage(
            @DestinationVariable UUID recipientId,
            @Payload ChatMessageDto chatMessageDto) {

        ChatMessageDto savedMessage = chatService.saveMessage(
                chatMessageDto.getChatSessionId(),
                chatMessageDto.getSenderId(),
                chatMessageDto.getContent(),
                chatMessageDto.getImageUrl()
        );

        messagingTemplate.convertAndSendToUser(
                recipientId.toString(),
                "/queue/messages",
                savedMessage
        );
    }

    /**
     * REST endpoint to create a new chat session
     */
    @PostMapping("/api/v1/chat/start/{consultationRequestId}/astrologer/{astrologerId}")
    public ResponseEntity<ChatSessionDto> startChat(
            @PathVariable UUID consultationRequestId,
            @PathVariable UUID astrologerId) {
        ChatSessionDto sessionDto = chatService.createChatSession(consultationRequestId, astrologerId);
        return ResponseEntity.ok(sessionDto);
    }

    /**
     * REST endpoint to end a chat session
     */
    @DeleteMapping("/api/v1/chat/end/{consultationRequestId}")
    public ResponseEntity<?> endChat(@PathVariable UUID consultationRequestId) {
        chatService.deleteChatSession(consultationRequestId);
        return ResponseEntity.noContent().build();
    }

    /**
     * REST endpoint to get all messages for a chat session
     */
    @GetMapping("/api/v1/chat/messages/{chatSessionId}")
    public ResponseEntity<List<ChatMessageDto>> getMessages(@PathVariable UUID chatSessionId) {
        return ResponseEntity.ok(chatService.getMessages(chatSessionId));
    }
}