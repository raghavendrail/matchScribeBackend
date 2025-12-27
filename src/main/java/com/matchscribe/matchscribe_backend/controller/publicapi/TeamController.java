package com.matchscribe.matchscribe_backend.controller.publicapi;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matchscribe.matchscribe_backend.entity.ApiResponse;
import com.matchscribe.matchscribe_backend.entity.Team;
import com.matchscribe.matchscribe_backend.service.TeamService;

@RestController
@RequestMapping("/api/teams")
public class TeamController {
	private final TeamService teamService;

	public TeamController(TeamService teamService) {
		this.teamService = teamService;
	}

	@GetMapping("/all-teams")
	public ResponseEntity<ApiResponse<List<Team>>> getAllTeams() {
		try {
			List<Team> teams = teamService.getAllTeams();
			return ResponseEntity.ok(new ApiResponse<>(true, "teams fetched successfully", teams));
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(500).body(new ApiResponse<>(false, "Failed to fetch teams", null));
		}
	}

	@GetMapping("/{slug}")
	public ResponseEntity<Team> getTeamBySlug(@PathVariable("slug") String slug) {
		Team dto = teamService.getTeamBySlug(slug);
		if (dto == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(dto);
	}

}
