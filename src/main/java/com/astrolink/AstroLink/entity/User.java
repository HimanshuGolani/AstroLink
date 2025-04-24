package com.astrolink.AstroLink.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

@Document(collection = "users")
public class User {
    @Id
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String role;
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    private String expertise;
    private double rating;

    @DBRef
    private Set<ConsultationRequest> consultationRequests = new TreeSet<>();

}
