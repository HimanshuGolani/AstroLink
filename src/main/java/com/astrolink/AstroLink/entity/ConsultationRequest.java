package com.astrolink.AstroLink.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Document(collection = "consultation-request")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationRequest implements Comparable<ConsultationRequest> {
    @Id
    private UUID id;
    private UUID userId;
    private String title;
    private String birthDate;
    private String birthTime;
    private String birthPlace;
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    private RequestStatus requestStatus = RequestStatus.PROGRESS;

//     for user only to accept the pending requests
    private List<UUID> toAcceptAstrologerIds = new ArrayList<>();
//  the current sets of astrologer ids that are accepted by users
    private List<UUID> acceptingAstrologersId = new ArrayList<>();
    @Setter
    private boolean openForAll;

    private LocalDateTime createdAt;

    public boolean isOpenForAll() {
        return acceptingAstrologersId.size() < 3;
    }


    @Override
    public int compareTo(ConsultationRequest o) {
        return o.getCreatedAt().compareTo(this.getCreatedAt());
    }
}
