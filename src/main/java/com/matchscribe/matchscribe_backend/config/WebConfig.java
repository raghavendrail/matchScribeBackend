package com.matchscribe.matchscribe_backend.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import com.matchscribe.matchscribe_backend.config.SportsApiConfig;

@Configuration
@EnableConfigurationProperties(SportsApiConfig.class)
public class WebConfig
{
	@Bean
    public WebClient sportsApiWebClient(SportsApiConfig props) {
		System.out.println("Configuring WebClient with baseUrl: " + props);
		WebClient output= WebClient.builder()
				.baseUrl(props.getBaseUrl())
				.defaultHeader("x-rapidapi-host", props.getHost())
				.defaultHeader("x-rapidapi-key", props.getApiKey())
				.build();
		System.out.println("WebClient configured: " + output);
		return output;
    }
}