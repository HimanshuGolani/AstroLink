package com.astrolink.AstroLink;

import com.astrolink.AstroLink.config.RsaConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@EnableConfigurationProperties(RsaConfigurationProperties.class)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@EnableWebSocketMessageBroker
public class AstroLinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(AstroLinkApplication.class, args);
	}

}
