package com.astrolink.AstroLink.service.impl;

import com.astrolink.AstroLink.dto.mapper.ChatMapper;
import com.astrolink.AstroLink.dto.request.PaymentRequestDto;
import com.astrolink.AstroLink.dto.response.*;
import com.astrolink.AstroLink.entity.ChatSession;
import com.astrolink.AstroLink.entity.ConsultationRequest;
import com.astrolink.AstroLink.entity.PaymentStatus;
import com.astrolink.AstroLink.entity.User;
import com.astrolink.AstroLink.exception.custom.UserBlockedException;
import com.astrolink.AstroLink.repository.ChatSessionRepository;
import com.astrolink.AstroLink.repository.ConsultationRequestRepository;
import com.astrolink.AstroLink.repository.UserRepository;
import com.astrolink.AstroLink.service.ChatService;
import com.astrolink.AstroLink.util.FinderClassUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final UserRepository userRepository;
    private final StripeServiceImpl stripeService;
    private final ChatMapper chatMapper;
    private final FinderClassUtil finderClassUtil;
    private final ConsultationRequestRepository consultationRequestRepository;


    @Transactional
    public ChatInitiationResponse createChat(UUID astrologerId, UUID consultationRequestId) {
        try {
        User astrologer = finderClassUtil.findUserById(astrologerId);
        ConsultationRequest consultationRequest = finderClassUtil.findConsultationRequestById(consultationRequestId);
        User consultationRequestCreator = finderClassUtil.findUserById(consultationRequest.getUserId());
        if (consultationRequestCreator.getBlockedAstrologerIds().contains(astrologerId)) {
            throw new UserBlockedException("Astrologer is blocked by the user, cannot accept this request");
        }
        int countOfConsultationRequests = finderClassUtil.findChatSessionById(consultationRequestId);
        ChatInitiationResponse response = new ChatInitiationResponse();
        boolean paymentRequired = (countOfConsultationRequests >= 4 && countOfConsultationRequests % 4 == 0) && consultationRequest.getPaymentStatus() != PaymentStatus.PAID;
        if (paymentRequired) {
            PaymentRequestDto paymentRequest = new PaymentRequestDto();
            paymentRequest.setAmount(300L);
            paymentRequest.setName("More than free chat requests");
            PaymentStatusResponseDto paymentStatusResponse = stripeService.checkoutProducts(paymentRequest);
            response.setPaymentStatus(paymentStatusResponse);
        }
        ChatSession chatSession = new ChatSession();
        chatSession.setId(UUID.randomUUID());
        chatSession.setUserId(consultationRequestCreator.getId());
        chatSession.setAstrologerId(astrologerId);
        chatSession.setConsultationRequestId(consultationRequestId);
        chatSessionRepository.save(chatSession);
        ChatSessionDto chatSessionDto = chatMapper.toDto(chatSession);

        consultationRequestCreator.getActiveChatSessionIds().add(chatSession.getId());
        userRepository.save(consultationRequestCreator);
        astrologer.getActiveChatSessionIds().add(chatSession.getId());
        userRepository.save(astrologer);

        consultationRequest.getToAcceptAstrologerIds().remove(astrologer);
        consultationRequest.getAcceptingAstrologersId().add(astrologer);
        consultationRequestRepository.save(consultationRequest);
        response.setChatSession(chatSessionDto);

        return response;
    }

         catch (UserBlockedException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            throw new RuntimeException("Failed to create chat: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error while creating chat", ex);
        }

    }

    @Override
    public List<SmallChatsResponseDto> getAllSmallChats(UUID userId) {
        User user = finderClassUtil.findUserById(userId);
            return  user.getConsultationRequestIds()
                        .stream()
                        .map(chatSessionRepository::findByConsultationsRequestId)
                        .filter(Objects::nonNull)
                        .flatMap(List::stream)
                        .map(chatMapper::toSmallChat)
                        .toList();
    }

    @Override
    public ChatDto getChatById(UUID userId, UUID chatId) {
        ChatSession chatSession = chatSessionRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat session not found"));

        // Ensure the user is a participant in the chat
        if (!chatSession.getUserId().equals(userId) && !chatSession.getAstrologerId().equals(userId)) {
            throw new RuntimeException("User is not authorized to view this chat");
        }

        return chatMapper.toChatDto(chatSession);
    }



}
