package com.astrolink.AstroLink.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "rsa")
public class RsaConfigurationProperties {
    private RSAPrivateKey rsaPrivateKey;
    private RSAPublicKey rsaPublicKey;
}
