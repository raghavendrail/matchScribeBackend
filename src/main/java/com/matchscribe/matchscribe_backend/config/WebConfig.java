package com.matchscribe.matchscribe_backend.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(SportsApiConfig.class)
public class WebConfig implements WebMvcConfigurer {
	@Bean
	public WebClient sportsApiWebClient() {

		System.out.println("Configuring Sports API WebClient (no baseUrl)");

		return WebClient.builder().defaultHeader("X-RapidAPI-Host", "cricbuzz-cricket.p.rapidapi.com")
				.defaultHeader("X-RapidAPI-Key", "DYNAMIC_FROM_PROPS").build();
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**").allowedOrigins("http://localhost:5173")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedHeaders("*");
	}
}