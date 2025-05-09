package com.astrolink.AstroLink.repository;

import com.astrolink.AstroLink.entity.ChatSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatSessionRepository extends MongoRepository<ChatSession, UUID> {
    Optional<ChatSession> findByConsultationRequestId(UUID consultationRequestId);

    @Query("{ 'userId' : ?1, 'astrologerId' : ?1, 'consultationRequestId' : ?2 }")
    List<ChatSession> findAllByUserIdAndAstrologerIdAndConsultationRequestId(
            UUID userId, UUID astrologerId, UUID consultationRequestId);
    @Query("{ 'consultationRequestId' : ?0 }")
    List<ChatSession> findByConsultationsRequestId(UUID consultationRequestId);
    void deleteByConsultationRequestId(UUID consultationRequestId);
    @Query("{ 'lastActive' : { $lt: ?0 } }")
    List<ChatSession> findByLastActiveBefore(LocalDateTime threshold);
}
