package com.astrolink.AstroLink.dto.mapper;

import com.astrolink.AstroLink.dto.request.MessageDto;
import com.astrolink.AstroLink.dto.response.ChatDto;
import com.astrolink.AstroLink.dto.response.ChatMessageDto;
import com.astrolink.AstroLink.dto.response.ChatSessionDto;
import com.astrolink.AstroLink.dto.response.SmallChatsResponseDto;
import com.astrolink.AstroLink.entity.ChatMessage;
import com.astrolink.AstroLink.entity.ChatSession;
import com.astrolink.AstroLink.entity.MessageType;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = {UUID.class, LocalDateTime.class, MessageType.class})
public interface ChatMapper {

    ChatMessageDto toDto(ChatMessage message);
    ChatMessage toEntity(ChatMessageDto messageDto);

    ChatSessionDto toDto(ChatSession session);
    ChatSession toEntity(ChatSessionDto sessionDto);

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "timestamp", expression = "java(LocalDateTime.now())")
    @Mapping(target = "type", expression = "java(MessageType.CHAT)")
    ChatMessage toEntity(MessageDto message);

    List<ChatMessageDto> toMessageDtoList(List<ChatMessage> messages);

    @Mapping(target = "chatId", source = "id")
    @Mapping(target = "chatName", expression = "java(\"Chat \" + chatSession.getId().toString().substring(0, 8))")
    @Mapping(target = "consultationId", source = "consultationRequestId")  // Map the consultation ID
    @Mapping(target = "lastUpdatedAt", source = "lastActive")
    SmallChatsResponseDto toSmallChat(ChatSession chatSession);

    @Mapping(target = "chatId", source = "id")
    @Mapping(target = "consultationId", source = "consultationRequestId")
    ChatDto toChatDto(ChatSession chatSession);
}