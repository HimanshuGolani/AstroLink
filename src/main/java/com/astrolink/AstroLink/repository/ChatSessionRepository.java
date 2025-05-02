package com.astrolink.AstroLink.repository;

import com.astrolink.AstroLink.entity.ChatSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatSessionRepository extends MongoRepository<ChatSession, UUID> {
    Optional<ChatSession> findByConsultationRequestId(UUID consultationRequestId);
    void deleteByConsultationRequestId(UUID consultationRequestId);
}
