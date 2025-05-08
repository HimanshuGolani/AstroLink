package com.astrolink.AstroLink.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "payment")
@Data
public class PaymentConfig {
    private Long standardChatFee = 5000L; // Default value: 50.00 INR
    private Long premiumChatFee = 10000L; // Default value: 100.00 INR
    private Long emergencyConsultationFee = 15000L; // Default value: 150.00 INR
    private String currency = "inr"; // Default currency

    // Additional payment thresholds or settings can be added here
    private Integer freeChatsLimit = 3; // Number of free chats before payment is required
}