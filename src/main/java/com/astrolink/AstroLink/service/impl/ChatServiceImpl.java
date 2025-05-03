package com.astrolink.AstroLink.service.impl;

import com.astrolink.AstroLink.dto.mapper.ChatMapper;
import com.astrolink.AstroLink.dto.response.ChatMessageDto;
import com.astrolink.AstroLink.dto.response.ChatSessionDto;
import com.astrolink.AstroLink.entity.ChatMessage;
import com.astrolink.AstroLink.entity.ChatSession;
import com.astrolink.AstroLink.entity.ConsultationRequest;
import com.astrolink.AstroLink.entity.MessageType;
import com.astrolink.AstroLink.exception.custom.DataNotFoundException;
import com.astrolink.AstroLink.repository.ChatMessageRepository;
import com.astrolink.AstroLink.repository.ChatSessionRepository;
import com.astrolink.AstroLink.repository.ConsultationRequestRepository;
import com.astrolink.AstroLink.repository.UserRepository;
import com.astrolink.AstroLink.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ConsultationRequestRepository consultationRequestRepository;
    private final UserRepository userRepository;
    private final ChatMapper chatMapper;

//    TODO: accept the chat request and create the session-id and send it
//    TODO: add a check for the payment as the number of active chats of more than *3 300rs every request

    @Override
    public ChatSessionDto createChatSession(UUID consultationRequestId, UUID astrologerId) {
        Optional<ChatSession> existingSession = chatSessionRepository.findByConsultationRequestId(consultationRequestId);
        if (existingSession.isPresent()) {
            return chatMapper.toDto(existingSession.get());
        }

        ConsultationRequest consultationRequest = consultationRequestRepository.findById(consultationRequestId)
                .orElseThrow(() -> new DataNotFoundException("ConsultationRequest not found"));

        UUID userId = consultationRequest.getUserId();

        ChatSession chatSession = ChatSession.builder()
                .id(UUID.randomUUID())
                .consultationRequestId(consultationRequestId)
                .userId(userId)
                .astrologerId(astrologerId)
                .startedAt(LocalDateTime.now())
                .build();

        ChatSession savedSession = chatSessionRepository.save(chatSession);

        // Update both users' activeChatSessionIds and acceptedConsultations
        userRepository.findById(astrologerId).ifPresent(astrologer -> {
            astrologer.getAcceptedConsultationIds().add(consultationRequestId);
            astrologer.getActiveChatSessionIds().add(savedSession.getId());
            userRepository.save(astrologer);
        });

        userRepository.findById(userId).ifPresent(user -> {
            user.getActiveChatSessionIds().add(savedSession.getId());
            userRepository.save(user);
        });

        return chatMapper.toDto(savedSession);
    }

    @Override
    public void deleteChatSession(UUID consultationRequestId) {
        chatSessionRepository.findByConsultationRequestId(consultationRequestId).ifPresent(session -> {
            // Delete all messages related to this session
            chatMessageRepository.deleteByChatSessionId(session.getId());

            // Remove session ID from both users' activeChatSessionIds
            UUID astrologerId = session.getAstrologerId();
            UUID userId = session.getUserId();

            userRepository.findById(astrologerId).ifPresent(astrologer -> {
                astrologer.getActiveChatSessionIds().remove(session.getId());
                userRepository.save(astrologer);
            });

            userRepository.findById(userId).ifPresent(user -> {
                user.getActiveChatSessionIds().remove(session.getId());
                userRepository.save(user);
            });

            chatSessionRepository.delete(session);
        });
    }

    @Override
    public ChatMessageDto saveMessage(UUID chatSessionId, UUID senderId, String content, String imageUrl) {
        return saveMessage(chatSessionId, senderId, content, imageUrl, MessageType.CHAT);
    }

    @Override
    public ChatMessageDto saveMessage(UUID chatSessionId, UUID senderId, String content, String imageUrl, MessageType type) {
        ChatMessage message = ChatMessage.builder()
                .id(UUID.randomUUID())
                .chatSessionId(chatSessionId)
                .senderId(senderId)
                .content(content)
                .imageUrl(imageUrl)
                .timestamp(LocalDateTime.now())
                .type(type)
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(message);
        return chatMapper.toDto(savedMessage);
    }

    @Override
    public List<ChatMessageDto> getMessages(UUID chatSessionId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatSessionId(chatSessionId);
        return messages.stream()
                .map(chatMapper::toDto)
                .collect(Collectors.toList());
    }
}