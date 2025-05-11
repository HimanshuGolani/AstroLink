package com.astrolink.AstroLink.dto.request;

import com.astrolink.AstroLink.entity.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private String content;
    private UUID senderId;
    private MessageType messageType;
    private List<String> imageUrls;
}
