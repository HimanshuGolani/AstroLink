package com.astrolink.AstroLink.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDto {
    private UUID id;
    private UUID chatSessionId;
    private UUID senderId;
    private String content;
    private String imageUrl;
    private LocalDateTime timestamp;
}
