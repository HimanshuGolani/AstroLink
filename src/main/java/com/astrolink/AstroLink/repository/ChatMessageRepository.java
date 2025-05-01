package com.astrolink.AstroLink.repository;

import com.astrolink.AstroLink.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, UUID> {
    List<ChatMessage> findByChatSessionId(UUID chatSessionId);
    void deleteByChatSessionId(UUID chatSessionId);
}
