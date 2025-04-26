package com.astrolink.AstroLink;

import com.astrolink.AstroLink.config.RsaConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaConfigurationProperties.class)
public class AstroLinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(AstroLinkApplication.class, args);
	}

}
