package com.astrolink.AstroLink.dto.request;

import com.astrolink.AstroLink.entity.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    private String content;
    private String sender;
    private MessageType messageType;
    private List<String> imageUrls;
}
