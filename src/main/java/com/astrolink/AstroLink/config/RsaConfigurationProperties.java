package com.astrolink.AstroLink.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Component
@ConfigurationProperties(prefix = "rsa")
public record RsaConfigurationProperties(RSAPrivateKey rsaPrivateKey, RSAPublicKey rsaPublicKey) {
}
