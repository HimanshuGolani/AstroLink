package com.astrolink.AstroLink.dto.mapper;

import com.astrolink.AstroLink.dto.request.MessageDto;
import com.astrolink.AstroLink.dto.response.ChatDto;
import com.astrolink.AstroLink.dto.response.ChatMessageDto;
import com.astrolink.AstroLink.dto.response.ChatSessionDto;
import com.astrolink.AstroLink.dto.response.SmallChatsResponseDto;
import com.astrolink.AstroLink.entity.ChatMessage;
import com.astrolink.AstroLink.entity.ChatSession;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ChatMapper {
    ChatMessageDto toDto(ChatMessage message);
    ChatMessage toEntity(ChatMessageDto messageDto);

    ChatSessionDto toDto(ChatSession session);
    ChatSession toEntity(ChatSessionDto sessionDto);
    ChatMessage toEntity(MessageDto message);
    List<ChatMessageDto> toMessageDtoList(List<ChatMessage> messages);
    SmallChatsResponseDto toSmallChat(ChatSession chatSession);
    ChatDto toChatDto(ChatSession chatSession);

}
