package com.matchscribe.matchscribe_backend.service;

import java.util.List;

import com.matchscribe.matchscribe_backend.entity.Team;

public interface TeamService {
	void importTeamPlayers(Long teamSl, Long teamId);

	void getTeamPlayers(Long teamSl, Long teamId, Long matchId);

	List<Team> getAllTeams();

	Team getTeamBySlug(String slug);
}
