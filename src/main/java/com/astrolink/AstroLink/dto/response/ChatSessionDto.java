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
public class ChatSessionDto {
    private UUID id;
    private UUID consultationRequestId;
    private UUID userId;
    private UUID astrologerId;
    private LocalDateTime startedAt;
}
