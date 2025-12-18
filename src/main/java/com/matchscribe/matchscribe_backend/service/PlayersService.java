package com.matchscribe.matchscribe_backend.service;

import com.matchscribe.matchscribe_backend.dto.team.PlayerDto;

public interface PlayersService {
	PlayerDto getPlayerById(Long playerId);

}
