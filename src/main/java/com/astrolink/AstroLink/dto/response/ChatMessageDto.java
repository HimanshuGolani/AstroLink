package com.astrolink.AstroLink.dto.response;

import com.astrolink.AstroLink.entity.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
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
    private List<String> imageUrl;
    private LocalDateTime timestamp;
    private MessageType type = MessageType.CHAT; // Default type
}