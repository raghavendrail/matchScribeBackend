package com.matchscribe.matchscribe_backend.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(OpenAiConfig.class)
public class OpenApiConfig {

	@Bean
	WebClient openAiWebClient(OpenAiConfig config) {
		return WebClient.builder().baseUrl(config.getBaseUrl())
				.defaultHeader("Authorization", "Bearer " + config.getApiKey())
				.defaultHeader("Content-Type", "application/json").build();
	}
}
