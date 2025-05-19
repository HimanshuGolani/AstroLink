package com.astrolink.AstroLink.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = extractToken(accessor);
            if (token != null) {
                try {
                    Jwt jwt = jwtDecoder.decode(token);
                    Authentication auth = jwtAuthenticationConverter.convert(jwt);
                    accessor.setUser(auth);
                    log.debug("WebSocket connection authenticated: {}", auth.getName());
                } catch (JwtException e) {
                    log.error("Invalid JWT token: {}", e.getMessage());
                    return null; // Reject message
                }
            } else {
                log.error("No JWT token in STOMP CONNECT message");
                return null; // Reject message
            }
        }
        return message;
    }

    private String extractToken(StompHeaderAccessor accessor) {
        // Try from Authorization header
        String authHeader = accessor.getFirstNativeHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        // Try from token header
        return accessor.getFirstNativeHeader("token");
    }
}