package com.astrolink.AstroLink.dto.mapper;

import com.astrolink.AstroLink.dto.response.ChatMessageDto;
import com.astrolink.AstroLink.dto.response.ChatSessionDto;
import com.astrolink.AstroLink.entity.ChatMessage;
import com.astrolink.AstroLink.entity.ChatSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface ChatMapper {
    ChatMessageDto toDto(ChatMessage message);
    ChatMessage toEntity(ChatMessageDto messageDto);

    ChatSessionDto toDto(ChatSession session);
    ChatSession toEntity(ChatSessionDto sessionDto);

    List<ChatMessageDto> toMessageDtoList(List<ChatMessage> messages);
}
