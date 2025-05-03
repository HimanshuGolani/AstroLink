package com.astrolink.AstroLink.repository;

import com.astrolink.AstroLink.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, UUID> {

}
