package com.astrolink.AstroLink.util;

import com.astrolink.AstroLink.dto.mapper.ChatMapper;
import com.astrolink.AstroLink.entity.ChatSession;
import com.astrolink.AstroLink.entity.ConsultationRequest;
import com.astrolink.AstroLink.entity.User;
import com.astrolink.AstroLink.exception.custom.DataNotFoundException;
import com.astrolink.AstroLink.repository.ChatSessionRepository;
import com.astrolink.AstroLink.repository.ConsultationRequestRepository;
import com.astrolink.AstroLink.repository.UserRepository;
import com.astrolink.AstroLink.service.impl.StripeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinderClassUtil {
    private final ChatSessionRepository chatSessionRepository;
    private final ConsultationRequestRepository consultationRequestRepository;
    private final UserRepository userRepository;

    public int findChatSessionById(UUID consultationId){
        return chatSessionRepository
                .findByConsultationsRequestId(consultationId).size();
    }
    public User findUserById(UUID userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
    }
    public ConsultationRequest findConsultationRequestById(UUID consultationRequestId){
        return consultationRequestRepository.findById(consultationRequestId)
                .orElseThrow(() -> new DataNotFoundException("Consultation Request not found"));
    }

    public ChatSession findChatById(UUID chatId){
        return chatSessionRepository.findById(
                chatId
        ).orElseThrow(() -> new DataNotFoundException("Chat not found."));
    }


}
