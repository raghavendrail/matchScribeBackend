package com.matchscribe.matchscribe_backend.controller.admin;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matchscribe.matchscribe_backend.service.OpenApiService;

@RestController
@RequestMapping("/openApi/ai")
public class OpenApiController {
	private final OpenApiService openAiService;

	public OpenApiController(OpenApiService openAiService) {
		this.openAiService = openAiService;
	}

	@PostMapping("/generate")
	public ResponseEntity<String> generate(@RequestBody Map<String, String> body) {
		if (body == null || body.isEmpty()) {
			return ResponseEntity.badRequest().body("Request body is empty");
		}

		// Merge all values into a single prompt
		String prompt = body.values().stream().filter(Objects::nonNull).collect(Collectors.joining(". "));
		return ResponseEntity.ok(openAiService.generateText(prompt));
	}
}
