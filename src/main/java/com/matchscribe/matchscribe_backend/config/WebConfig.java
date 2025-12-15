package com.matchscribe.matchscribe_backend.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(SportsApiConfig.class)
public class WebConfig {
	@Bean
	public WebClient sportsApiWebClient() {

		System.out.println("Configuring Sports API WebClient (no baseUrl)");

		return WebClient.builder().defaultHeader("X-RapidAPI-Host", "cricbuzz-cricket.p.rapidapi.com")
				.defaultHeader("X-RapidAPI-Key", "DYNAMIC_FROM_PROPS").build();
	}
}