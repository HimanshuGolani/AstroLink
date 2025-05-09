package com.astrolink.AstroLink.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "chat")
@Data
public class ChatConfig {
    private int inactivityDays = 1; // Default value
}