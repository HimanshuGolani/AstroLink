package com.astrolink.AstroLink.controller;

import com.astrolink.AstroLink.dto.response.ChatInitiationResponse;
import com.astrolink.AstroLink.dto.response.ChatSessionDto;
import com.astrolink.AstroLink.service.ChatService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

//     create room
@PostMapping("/start")
public ResponseEntity<ChatInitiationResponse> createChat(
        @RequestParam UUID astrologerId,
        @RequestParam UUID consultationRequestId
) {
    ChatInitiationResponse chatSession = chatService.createChat(astrologerId, consultationRequestId);
    return ResponseEntity.ok(chatSession);
}
//    get list of chat of user small just chatId and name of chat
    @GetMapping("/")
    public
//    get a chat perticular chat by the id of it including messages

}
