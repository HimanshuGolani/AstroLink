package com.astrolink.AstroLink.repository;

import com.astrolink.AstroLink.entity.ConsultationRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConsultationRequestRepository extends MongoRepository<ConsultationRequest, UUID> {
    List<ConsultationRequest> findByUserId(UUID userId);
}
