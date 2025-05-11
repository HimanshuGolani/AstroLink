package com.astrolink.AstroLink.service.impl;

import com.astrolink.AstroLink.config.PaymentConfig;
import com.astrolink.AstroLink.dto.mapper.ChatMapper;
import com.astrolink.AstroLink.dto.request.PaymentRequestDto;
import com.astrolink.AstroLink.dto.response.*;
import com.astrolink.AstroLink.entity.ChatSession;
import com.astrolink.AstroLink.entity.ConsultationRequest;
import com.astrolink.AstroLink.entity.PaymentStatus;
import com.astrolink.AstroLink.entity.User;
import com.astrolink.AstroLink.exception.custom.*;
import com.astrolink.AstroLink.repository.ChatSessionRepository;
import com.astrolink.AstroLink.repository.ConsultationRequestRepository;
import com.astrolink.AstroLink.repository.UserRepository;
import com.astrolink.AstroLink.service.ChatService;
import com.astrolink.AstroLink.util.FinderClassUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatSessionRepository chatSessionRepository;
    private final UserRepository userRepository;
    private final StripeServiceImpl stripeService;
    private final ChatMapper chatMapper;
    private final FinderClassUtil finderClassUtil;
    private final ConsultationRequestRepository consultationRequestRepository;
    private final PaymentConfig paymentConfig;

    @Transactional
    public ChatInitiationResponse createChat(UUID astrologerId, UUID consultationRequestId) {
        try {
            // Find the necessary entities
            User astrologer = finderClassUtil.findUserById(astrologerId);
            if (astrologer == null) {
                throw new UserNotFoundException("Astrologer with ID " + astrologerId + " not found");
            }

            ConsultationRequest consultationRequest = finderClassUtil.findConsultationRequestById(consultationRequestId);
            if (consultationRequest == null) {
                throw new DataNotFoundException("Consultation request with ID " + consultationRequestId + " not found");
            }

            User consultationRequestCreator = finderClassUtil.findUserById(consultationRequest.getUserId());
            if (consultationRequestCreator == null) {
                throw new UserNotFoundException("User with ID " + consultationRequest.getUserId() + " not found");
            }

            // Check for duplicate chat sessions - FIXED to handle multiple results
            try {
                // Use a list instead of Optional to handle potential multiple results
                List<ChatSession> existingSessions = chatSessionRepository.findAllByUserIdAndAstrologerIdAndConsultationRequestId(
                        consultationRequestCreator.getId(), astrologerId, consultationRequestId);

                if (!existingSessions.isEmpty()) {
                    throw new ResourceAlreadyExists("A chat session already exists between this user and astrologer for this consultation");
                }
            } catch (Exception ex) {
                // If there's an error with the query (like non-unique results), log it and continue with a more basic check
                log.warn("Error checking for existing chat sessions: {}. Falling back to simpler check.", ex.getMessage());

                // Fallback: Get all sessions for this consultation and check manually
                List<ChatSession> allConsultationSessions = chatSessionRepository.findByConsultationsRequestId(consultationRequestId);
                boolean chatExists = allConsultationSessions.stream()
                        .anyMatch(session ->
                                session.getUserId().equals(consultationRequestCreator.getId()) &&
                                        session.getAstrologerId().equals(astrologerId));

                if (chatExists) {
                    throw new ResourceAlreadyExists("A chat session already exists between this user and astrologer for this consultation");
                }
            }

            // Check if user has blocked the astrologer
            if (consultationRequestCreator.getBlockedAstrologerIds().contains(astrologerId)) {
                throw new UserBlockedException("Astrologer is blocked by the user, cannot accept this request");
            }

            // Check if payment is required
            int countOfConsultationRequests = finderClassUtil.findChatSessionById(consultationRequestId);
            ChatInitiationResponse response = new ChatInitiationResponse();

            boolean paymentRequired = (countOfConsultationRequests >= paymentConfig.getFreeChatsLimit() &&
                    countOfConsultationRequests % paymentConfig.getFreeChatsLimit() == 0) &&
                    consultationRequest.getPaymentStatus() != PaymentStatus.PAID;

            if (paymentRequired) {
                PaymentRequestDto paymentRequest = new PaymentRequestDto();

                // Calculate appropriate payment amount based on user and consultation type
                Long paymentAmount = calculatePaymentAmount(consultationRequest, consultationRequestCreator);

                paymentRequest.setAmount(paymentAmount);
                paymentRequest.setName("Additional Chat Session");
                paymentRequest.setCurrency(paymentConfig.getCurrency());

                log.info("Creating payment request for consultation {}: amount={} {}",
                        consultationRequestId, paymentAmount, paymentConfig.getCurrency());

                PaymentStatusResponseDto paymentStatusResponse = stripeService.checkoutProducts(paymentRequest);
                response.setPaymentStatus(paymentStatusResponse);
            }

            // Create and save the chat session
            ChatSession chatSession = new ChatSession();
            chatSession.setId(UUID.randomUUID());
            chatSession.setUserId(consultationRequestCreator.getId());
            chatSession.setAstrologerId(astrologerId);
            chatSession.setConsultationRequestId(consultationRequestId);
            chatSession.setLastActive(LocalDateTime.now());

            log.debug("Creating new chat session: {}", chatSession);
            chatSessionRepository.save(chatSession);

            ChatSessionDto chatSessionDto = chatMapper.toDto(chatSession);

            // Update user active chat sessions
            consultationRequestCreator.getActiveChatSessionIds().add(chatSession.getId());
            userRepository.save(consultationRequestCreator);

            astrologer.getActiveChatSessionIds().add(chatSession.getId());
            astrologer.getAcceptedConsultationIds().add(consultationRequestId);
            userRepository.save(astrologer);

            // Update consultation request
            consultationRequest.getToAcceptAstrologerIds().remove(astrologerId);
            consultationRequest.getAcceptingAstrologersId().add(astrologerId);
            consultationRequestRepository.save(consultationRequest);

            response.setChatSession(chatSessionDto);

            log.info("Successfully created chat session {} between user {} and astrologer {}",
                    chatSession.getId(), consultationRequestCreator.getId(), astrologerId);

            return response;

        } catch (DataNotFoundException | UserBlockedException | ResourceAlreadyExists ex) {
            // These exceptions are already properly typed, so just rethrow them
            log.error("Error creating chat: {}", ex.getMessage());
            throw ex;
        } catch (PaymentException ex) {
            log.error("Payment error while creating chat: {}", ex.getMessage());
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Runtime error creating chat: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to create chat: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<SmallChatsResponseDto> getAllSmallChats(UUID userId) {
        try {
            User user = finderClassUtil.findUserById(userId);
            if (user == null) {
                throw new UserNotFoundException("User with ID " + userId + " not found");
            }

            log.debug("Fetching all chats for user {}", userId);
            log.debug("User has {} consultation request IDs", user.getConsultationRequestIds().size());

            return user.getConsultationRequestIds()
                    .stream()
                    .map(consultationId -> {
                        List<ChatSession> sessions = chatSessionRepository.findByConsultationsRequestId(consultationId);
                        ConsultationRequest request = consultationRequestRepository.findById(consultationId).orElse(null); // fetch the request
                        log.debug("Found {} chat sessions for consultation ID: {}", sessions.size(), consultationId);
                        return sessions.stream()
                                .map(chat -> chatMapper.toSmallChat(chat, request)) // pass both chat and request
                                .toList();
                    })
                    .flatMap(List::stream)
                    .toList();


        } catch (UserNotFoundException ex) {
            log.error("User not found while getting chats: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error getting all chats for user {}: {}", userId, ex.getMessage(), ex);
            throw new RuntimeException("Failed to retrieve chats: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<SmallChatsResponseDto> getAllSmallChatsAstrologer(UUID astrologerId) {
        try {
            User astrologer = finderClassUtil.findUserById(astrologerId);
            if (astrologer == null) {
                throw new UserNotFoundException("User with ID " + astrologerId + " not found");
            }

            return astrologer.getActiveChatSessionIds()
                    .stream()
                    .map(chatSessionRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(chat -> {
                        ConsultationRequest request = consultationRequestRepository
                                .findById(chat.getConsultationRequestId())
                                .orElseThrow(() -> new DataNotFoundException(
                                        "Consultation request not found for chat session " + chat.getId()));
                        return chatMapper.toSmallChat(chat, request);
                    })
                    .toList();



        } catch (UserNotFoundException ex) {
            log.error("User not found while getting chats: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to retrieve chats: " + ex.getMessage(), ex);
        }
    }


    @Override
    public ChatDto getChatById(UUID userId, UUID chatId) {
        try {
            ChatSession chatSession = chatSessionRepository.findById(chatId)
                    .orElseThrow(() -> new DataNotFoundException("Chat session with ID " + chatId + " not found"));

            // Ensure the user is a participant in the chat
            if (!chatSession.getUserId().equals(userId) && !chatSession.getAstrologerId().equals(userId)) {
                throw new AccessDeniedException("User is not authorized to view this chat");
            }

            log.debug("Retrieving chat {} for user {}", chatId, userId);
            return chatMapper.toChatDto(chatSession);

        } catch (DataNotFoundException | AccessDeniedException ex) {
            log.error("Error retrieving chat: {}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Unexpected error retrieving chat {}: {}", chatId, ex.getMessage(), ex);
            throw new RuntimeException("Failed to retrieve chat: " + ex.getMessage(), ex);
        }
    }

    /**
     * Calculate the appropriate payment amount based on consultation type and user status
     * This method can be expanded to implement more complex pricing logic
     */
    private Long calculatePaymentAmount(ConsultationRequest consultationRequest, User user) {
        try {
            // Example of more complex pricing logic
            if (user.getPaymentStatus() == PaymentStatus.PAID) {
                log.debug("Using premium chat fee for paid user");
                return paymentConfig.getPremiumChatFee();
            } else {
                log.debug("Using standard chat fee");
                return paymentConfig.getStandardChatFee();
            }
        } catch (Exception ex) {
            log.error("Error calculating payment amount: {}", ex.getMessage(), ex);
            // Default to standard fee if there's an error in calculation
            return paymentConfig.getStandardChatFee();
        }
    }

}