package com.astrolink.AstroLink.dto.response;

import jdk.dynalink.linker.LinkerServices;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {
    private UUID chatId;
    private UUID userId;
    private UUID astrologerId;
    private UUID consultationId;
    private List<ChatMessageDto> messages;
}
