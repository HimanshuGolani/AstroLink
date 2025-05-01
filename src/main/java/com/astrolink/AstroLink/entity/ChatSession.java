package com.astrolink.AstroLink.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
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
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}
