package com.astrolink.AstroLink.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    @Id
    private UUID id;
    private UUID chatSessionId;
    private UUID senderId;
    private String content;
    private String imageUrl;
    private LocalDateTime timestamp;
    private MessageType type = MessageType.CHAT; // Default type
}