package com.matchscribe.matchscribe_backend.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.matchscribe.matchscribe_backend.dto.match.MatchDto;
import com.matchscribe.matchscribe_backend.service.MatchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/matches")
public class MatchAdminController {
	
	private final MatchService matchService;
	public MatchAdminController(MatchService matchService) {
		this.matchService = matchService;
	}

    @PostMapping("/import-from-sportsapi")
    public ResponseEntity<Void> importFromSportsApi() {
        matchService.importUpcomingMatchesFromSportsApi();
        return ResponseEntity.accepted().build();
    }
        
        //GET /api/admin/matches/{id}
        @GetMapping("/{matchId}")
        public ResponseEntity<MatchDto> getMatchById(@PathVariable("matchId") Long matchId) {
            MatchDto dto = matchService.getMatchById(matchId);
            if (dto == null || dto.match == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(dto);
        }
        
        //GET /api/admin/matches 
        @GetMapping("matches")
        public ResponseEntity<Iterable<MatchDto>> getAllMatches() {
			Iterable<MatchDto> dtos = matchService.getAllMatches();
			return ResponseEntity.ok(dtos);
		}

}
