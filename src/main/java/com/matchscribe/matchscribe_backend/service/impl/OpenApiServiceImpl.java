package com.matchscribe.matchscribe_backend.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.matchscribe.matchscribe_backend.config.OpenAiConfig;
import com.matchscribe.matchscribe_backend.service.OpenApiService;

@Service
public class OpenApiServiceImpl implements OpenApiService {
	private final WebClient openAiWebClient;
	private final OpenAiConfig config;

	public OpenApiServiceImpl(WebClient openAiWebClient, OpenAiConfig config) {
		this.openAiWebClient = openAiWebClient;
		this.config = config;
	}

	@Override
	public String generateText(String input) {

		Map<String, Object> request = Map.of("model", config.getModel(), "input", input);

		return openAiWebClient.post().uri("/responses").bodyValue(request).retrieve().bodyToMono(String.class).block();
	}

}
