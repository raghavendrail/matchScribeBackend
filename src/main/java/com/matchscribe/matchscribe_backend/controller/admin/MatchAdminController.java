package com.matchscribe.matchscribe_backend.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matchscribe.matchscribe_backend.dto.match.MatchDto;
import com.matchscribe.matchscribe_backend.service.MatchService;
import com.matchscribe.matchscribe_backend.util.ImportResult;

@RestController
@RequestMapping("/api/admin/matches")
public class MatchAdminController {

	private final MatchService matchService;

	public MatchAdminController(MatchService matchService) {
		this.matchService = matchService;
	}

	@PostMapping("/import-from-sportsapi")
	public ResponseEntity<?> importFromSportsApi() {

		ImportResult result = matchService.importUpcomingMatchesFromSportsApi();

		if (!result.getErrors().isEmpty()) {
			return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(result);
		}

		return ResponseEntity.ok(result);
	}

	// GET /api/admin/matches/{id}
	@GetMapping("/{matchId}")
	public ResponseEntity<MatchDto> getMatchById(@PathVariable("matchId") Long matchId) {
		MatchDto dto = matchService.getMatchById(matchId);
		if (dto == null || dto.match == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(dto);
	}

	// GET /api/admin/matches
	@GetMapping("matches")
	public ResponseEntity<Iterable<MatchDto>> getAllMatches() {
		Iterable<MatchDto> dtos = matchService.getAllMatches();
		return ResponseEntity.ok(dtos);
	}

}
