package com.astrolink.AstroLink.dto.mapper;

import com.astrolink.AstroLink.dto.response.ChatMessageDto;
import com.astrolink.AstroLink.dto.response.ChatSessionDto;
import com.astrolink.AstroLink.entity.ChatMessage;
import com.astrolink.AstroLink.entity.ChatSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    ChatMessageDto toDto(ChatMessage message);

    @Mapping(target = "type", source = "type")
    ChatMessage toEntity(ChatMessageDto messageDto);

    ChatSessionDto toDto(ChatSession session);
    ChatSession toEntity(ChatSessionDto sessionDto);

    List<ChatMessageDto> toMessageDtoList(List<ChatMessage> messages);
}