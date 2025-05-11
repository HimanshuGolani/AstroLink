package com.astrolink.AstroLink.service.impl;

import com.astrolink.AstroLink.dto.mapper.ChatMapper;
import com.astrolink.AstroLink.dto.request.MessageDto;
import com.astrolink.AstroLink.dto.response.ChatMessageDto;
import com.astrolink.AstroLink.entity.ChatMessage;
import com.astrolink.AstroLink.entity.ChatSession;
import com.astrolink.AstroLink.repository.ChatMessageRepository;
import com.astrolink.AstroLink.repository.ChatSessionRepository;
import com.astrolink.AstroLink.service.MessagingService;
import com.astrolink.AstroLink.util.FinderClassUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessagingService {

    private final ChatMessageRepository messageRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final FinderClassUtil finderClassUtil;
    private final ChatMapper chatMapper;

    @Transactional
    @Override
    public ChatMessageDto publishMessage(UUID chatID, MessageDto messageRequest) {
       try{
           ChatSession chat = finderClassUtil.findChatById(chatID);
           ChatMessage message = chatMapper.toEntity(messageRequest);
           message.setId(UUID.randomUUID());
           message.setTimestamp(LocalDateTime.now());
           messageRepository.save(message);
           chat.getMessages().add(message);
           chatSessionRepository.save(chat);
           return chatMapper.toDto(message);
       }
       catch (RuntimeException e){
           throw new RuntimeException(e.getMessage());
       }
    }
}
