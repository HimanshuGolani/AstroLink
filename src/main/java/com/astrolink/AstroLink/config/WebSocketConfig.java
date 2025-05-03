package com.astrolink.AstroLink.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // This is the endpoint the client connects to (like ws://localhost:8080/ws)
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Prefix for messages coming from client to server (controller)
        registry.setApplicationDestinationPrefixes("/app");

        // Prefix for messages being sent from server to clients (subscriptions)
        registry.enableSimpleBroker("/topic"); // Enables /topic for broadcasting
    }

}
