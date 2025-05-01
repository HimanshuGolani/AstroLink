package com.astrolink.AstroLink.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

@Document(collection = "users")
@Data
public class User {
    @Id
    private UUID id;
    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String phoneNumber;
    private Role role;
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    private String expertise;
    private double rating;

    private Set<UUID> consultationRequestIds = new TreeSet<>();
    private Set<UUID> blockedAstrologerIds = new HashSet<>();
    private Set<UUID> acceptedConsultationIds = new HashSet<>();
    private Set<UUID> activeChatSessionIds = new HashSet<>();
}
