package com.astrolink.AstroLink.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmallChatsResponseDto {
    private UUID chatId;
    private String chatName;
    private String issue;
    private UUID consultationId;
    private LocalDateTime lastUpdatedAt;
}
