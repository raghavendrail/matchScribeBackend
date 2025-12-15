package com.matchscribe.matchscribe_backend.service;

public interface TeamService {
	void importTeamPlayers(Long teamSl, Long teamId);

	void getTeamPlayers(Long teamSl, Long teamId, Long matchId);
}
