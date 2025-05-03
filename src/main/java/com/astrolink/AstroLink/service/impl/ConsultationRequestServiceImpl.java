package com.astrolink.AstroLink.service.impl;

import com.astrolink.AstroLink.dto.mapper.ConsultationRequestMapper;
import com.astrolink.AstroLink.dto.request.ConsultationRequestCreateDto;
import com.astrolink.AstroLink.dto.response.ConsultationRequestDto;
import com.astrolink.AstroLink.entity.ChatSession;
import com.astrolink.AstroLink.entity.ConsultationRequest;
import com.astrolink.AstroLink.entity.PaymentStatus;
import com.astrolink.AstroLink.entity.User;
import com.astrolink.AstroLink.exception.custom.DataNotFoundException;
import com.astrolink.AstroLink.exception.custom.RequestNotAcceptingException;
import com.astrolink.AstroLink.exception.custom.UserBlockedException;
import com.astrolink.AstroLink.exception.custom.UserNotFoundException;
import com.astrolink.AstroLink.repository.ChatSessionRepository;
import com.astrolink.AstroLink.repository.ConsultationRequestRepository;
import com.astrolink.AstroLink.repository.UserRepository;
import com.astrolink.AstroLink.service.ConsultationRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultationRequestServiceImpl implements ConsultationRequestService {

    private final ConsultationRequestRepository consultationRequestRepository;
    private final UserRepository userRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final ConsultationRequestMapper consultationRequestMapper;

    @Override
    @Transactional
    public ConsultationRequestDto createConsultationRequest(UUID userId, ConsultationRequestCreateDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        ConsultationRequest consultationRequest = consultationRequestMapper.toEntity(requestDto);
        consultationRequest.setId(UUID.randomUUID());
        consultationRequest.setUserId(userId);
        consultationRequest.setCreatedAt(LocalDateTime.now());
        consultationRequest.setAcceptingAstrologersId(new ArrayList<>());
        consultationRequest.setPaymentStatus(PaymentStatus.PENDING);
        consultationRequest.setOpenForAll(true);

        ConsultationRequest savedRequest = consultationRequestRepository.save(consultationRequest);

        // Update user's consultation request IDs
        user.getConsultationRequestIds().add(savedRequest.getId());
        userRepository.save(user);

        return consultationRequestMapper.toDto(savedRequest);
    }

    @Override
    @Transactional
    public ConsultationRequestDto acceptConsultationRequest(UUID requestId, UUID astrologerId) {
        ConsultationRequest request = consultationRequestRepository.findById(requestId)
                .orElseThrow(() -> new DataNotFoundException("Consultation request not found with ID: " + requestId));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + request.getUserId()));

        User astrologer = userRepository.findById(astrologerId)
                .orElseThrow(() -> new UserNotFoundException("Astrologer not found with ID: " + astrologerId));

        // Check if astrologer is blocked by user
        if (user.getBlockedAstrologerIds().contains(astrologerId)) {
            throw new UserBlockedException("Cannot accept this request as you are blocked by the user");
        }

        // Check if the request is still open for acceptance
        if (!request.isOpenForAll()) {
            throw new RequestNotAcceptingException("This request is no longer accepting astrologers");
        }

        // Add astrologer to accepting astrologers
        request.getAcceptingAstrologersId().add(astrologer);

        // If we've reached the maximum number of astrologers, update isOpenForAll
        if (request.getAcceptingAstrologersId().size() >= 3) {
            request.setOpenForAll(false);
        }

        ConsultationRequest updatedRequest = consultationRequestRepository.save(request);

        // Update astrologer's accepted consultation IDs
        astrologer.getAcceptedConsultationIds().add(requestId);
        userRepository.save(astrologer);

        // Create a chat session
        ChatSession chatSession = ChatSession.builder()
                .id(UUID.randomUUID())
                .consultationRequestId(requestId)
                .userId(user.getId())
                .astrologerId(astrologerId)
                .lastActive(LocalDateTime.now())
                .build();

        chatSessionRepository.save(chatSession);

        // Update active chat sessions for both user and astrologer
        user.getActiveChatSessionIds().add(chatSession.getId());
        astrologer.getActiveChatSessionIds().add(chatSession.getId());

        userRepository.save(user);
        userRepository.save(astrologer);

        return consultationRequestMapper.toDto(updatedRequest);
    }

    @Override
    public List<ConsultationRequestDto> getAllAvailableRequests() {
        List<ConsultationRequest> allRequests = consultationRequestRepository.findAll();

        // Filter requests that are still open for acceptance
        List<ConsultationRequest> availableRequests = allRequests.stream()
                .filter(ConsultationRequest::isOpenForAll)
                .collect(Collectors.toList());

        return consultationRequestMapper.toDtoList(availableRequests);
    }

    @Override
    public List<ConsultationRequestDto> getUserRequests(UUID userId) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        List<ConsultationRequest> userRequests = consultationRequestRepository.findByUserId(userId);
        return consultationRequestMapper.toDtoList(userRequests);
    }

    @Override
    @Transactional
    public void closeRequest(UUID requestId) {
        ConsultationRequest request = consultationRequestRepository.findById(requestId)
                .orElseThrow(() -> new DataNotFoundException("Consultation request not found with ID: " + requestId));

        // Find all chat sessions related to this request
        List<ChatSession> chatSessions = chatSessionRepository.findByConsultationsRequestId(requestId);

        // Close all chat sessions
        for (ChatSession session : chatSessions) {
            // Remove from active chat sessions for both user and astrologer
            User user = userRepository.findById(session.getUserId()).orElse(null);
            User astrologer = userRepository.findById(session.getAstrologerId()).orElse(null);

            if (user != null) {
                user.getActiveChatSessionIds().remove(session.getId());
                userRepository.save(user);
            }

            if (astrologer != null) {
                astrologer.getActiveChatSessionIds().remove(session.getId());
                userRepository.save(astrologer);
            }
        }

        // Update the request to be closed (no longer open for all)
        request.setOpenForAll(false);
        request.getAcceptingAstrologersId().clear();
        consultationRequestRepository.save(request);

        // Save updated chat sessions
        chatSessionRepository.saveAll(chatSessions);
    }
}