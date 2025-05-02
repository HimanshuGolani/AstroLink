package com.astrolink.AstroLink.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Important: Make sure this endpoint is correctly configured
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")  // For development only, restrict in production
                .withSockJS();

        // Alternative endpoint
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*");  // Raw WebSocket endpoint without SockJS
    }
}