package com.matchscribe.matchscribe_backend.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matchscribe.matchscribe_backend.dto.team.PlayerDto;
import com.matchscribe.matchscribe_backend.service.PlayersService;

@RestController
@RequestMapping("/api/admin/player")
public class PlayerAdminController {
	private final PlayersService playerService;

	public PlayerAdminController(PlayersService playerService) {
		this.playerService = playerService;
	}

	@GetMapping("/{playerId}")
	public ResponseEntity<PlayerDto> getMatchById(@PathVariable("playerId") Long playerId) {
		PlayerDto dto = playerService.getPlayerById(playerId);
		if (dto == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(dto);
	}
}
