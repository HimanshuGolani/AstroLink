package com.astrolink.AstroLink.controller;

import com.astrolink.AstroLink.dto.response.ChatMessageDto;
import com.astrolink.AstroLink.dto.response.ChatSessionDto;
import com.astrolink.AstroLink.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDto sendMessage(@Payload ChatMessageDto chatMessageDto) {
        return chatService.saveMessage(
                chatMessageDto.getChatSessionId(),
                chatMessageDto.getSenderId(),
                chatMessageDto.getContent(),
                chatMessageDto.getImageUrl()
        );
    }

    @PostMapping("/chat/start/{consultationRequestId}/astrologer/{astrologerId}")
    public ResponseEntity<ChatSessionDto> startChat(@PathVariable UUID consultationRequestId, @PathVariable UUID astrologerId) {
        ChatSessionDto sessionDto = chatService.createChatSession(consultationRequestId, astrologerId);
        return ResponseEntity.ok(sessionDto);
    }

    @DeleteMapping("/chat/end/{consultationRequestId}")
    public ResponseEntity<?> endChat(@PathVariable UUID consultationRequestId) {
        chatService.deleteChatSession(consultationRequestId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/chat/messages/{chatSessionId}")
    public ResponseEntity<List<ChatMessageDto>> getMessages(@PathVariable UUID chatSessionId) {
        return ResponseEntity.ok(chatService.getMessages(chatSessionId));
    }
}
