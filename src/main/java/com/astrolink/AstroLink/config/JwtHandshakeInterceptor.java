package com.astrolink.AstroLink.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtDecoder jwtDecoder;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            String token = extractToken(servletRequest);

            if (token != null) {
                try {
                    Jwt jwt = jwtDecoder.decode(token);
                    attributes.put("user", jwt.getSubject());
                    log.debug("WebSocket handshake authenticated for user: {}", jwt.getSubject());
                    return true;
                } catch (JwtException e) {
                    log.error("Invalid JWT token in WebSocket handshake: {}", e.getMessage());
                    return false;
                }
            }
            log.error("No JWT token found in WebSocket handshake");
            return false;
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // No action needed
    }

    private String extractToken(HttpServletRequest request) {
        // Try from URL parameter
        String token = request.getParameter("token");
        if (token != null && !token.isEmpty()) {
            return token;
        }

        // Try from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }
}