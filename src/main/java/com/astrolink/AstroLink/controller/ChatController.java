package com.astrolink.AstroLink.controller;

import com.astrolink.AstroLink.dto.response.ChatDto;
import com.astrolink.AstroLink.dto.response.ChatInitiationResponse;
import com.astrolink.AstroLink.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/start")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ASTROLOGER')")
    @Operation(summary = "Start a new chat (The user will accept the request)", description = "Creates a chat between user and astrologer using a consultation request.")
    public ResponseEntity<ChatInitiationResponse> createChat(
            @RequestParam @Parameter(description = "Astrologer UUID") UUID astrologerId,
            @RequestParam @Parameter(description = "Consultation Request UUID") UUID consultationRequestId
    ) {
        ChatInitiationResponse chatSession = chatService.createChat(astrologerId, consultationRequestId);
        return ResponseEntity.ok(chatSession);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ASTROLOGER')")
    @Operation(summary = "Get all small chats for a user", description = "Returns a list of basic chat information for the user.")
    public ResponseEntity<?> getAllSmallChats(
            @PathVariable @Parameter(description = "User UUID") UUID userId
    ) {
        return new ResponseEntity<>(chatService.getAllSmallChats(userId), HttpStatus.OK);
    }

    @GetMapping("/{userId}/{chatId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ASTROLOGER')")
    @Operation(summary = "Get a specific chat session", description = "Fetches a chat session by userId and chatId, including messages.")
    public ResponseEntity<ChatDto> getChat(
            @PathVariable @Parameter(description = "User UUID") UUID userId,
            @PathVariable @Parameter(description = "Chat Session UUID") UUID chatId
    ) {
        ChatDto chatDto = chatService.getChatById(userId, chatId);
        return ResponseEntity.ok(chatDto);
    }
}
