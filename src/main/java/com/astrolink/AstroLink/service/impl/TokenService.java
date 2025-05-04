package com.astrolink.AstroLink.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtEncoder jwtEncoder;

    public String generateToken(Authentication authentication, UUID userId){
        Instant now = Instant.now();

        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet
                .builder()
                .issuer("Astrolink")
                .issuedAt(now)
                .subject(authentication.getName())
                .claim("roles", roles)
                .claim("userId", userId.toString())
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .build();

        return jwtEncoder
                .encode(JwtEncoderParameters
                        .from(jwtClaimsSet))
                .getTokenValue();
    }
}
