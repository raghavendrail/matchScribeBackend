package com.matchscribe.matchscribe_backend.controller.publicapi;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matchscribe.matchscribe_backend.dto.match.MatchDto;
import com.matchscribe.matchscribe_backend.entity.ApiResponse;
import com.matchscribe.matchscribe_backend.service.MatchService;

@RestController
@RequestMapping("/api/public")
public class MatchPublicController {
	private final MatchService matchService;

	public MatchPublicController(MatchService matchService) {
		this.matchService = matchService;
	}

	@GetMapping("/matches/upcoming")
	public ResponseEntity<ApiResponse<List<MatchDto>>> getUpcomingMatches() {

		try {
			List<MatchDto> matches = matchService.getUpcomingMatches();

			if (matches.isEmpty()) {
				return ResponseEntity.ok(new ApiResponse<>(true, "No upcoming matches found", Collections.emptyList()));
			}

			return ResponseEntity.ok(new ApiResponse<>(true, "Upcoming matches fetched successfully", matches));

		} catch (Exception ex) {
			// log error properly
			ex.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse<>(false, "Failed to fetch upcoming matches", null));
		}

	}

	// get match through slug
	@GetMapping("/match/{slug}")
	public ResponseEntity<MatchDto> getMatchBySlug(@PathVariable("slug") String slug) {
		MatchDto dto = matchService.getMatchBySlug(slug);
		if (dto == null || dto.match == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(dto);
	}
}
