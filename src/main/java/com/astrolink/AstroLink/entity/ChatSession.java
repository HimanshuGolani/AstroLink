package com.astrolink.AstroLink.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "chat_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatSession {
    @Id
    private UUID id;
    private UUID consultationRequestId;
    private UUID userId;
    private UUID astrologerId;
    @DBRef
    List<ChatMessage> messages = new ArrayList<>();
    private LocalDateTime lastActive;
}
