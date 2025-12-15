package com.matchscribe.matchscribe_backend.dto.match;

import java.util.List;

import com.matchscribe.matchscribe_backend.dto.team.PlayerDto;
import com.matchscribe.matchscribe_backend.entity.Match;
import com.matchscribe.matchscribe_backend.entity.Series;
import com.matchscribe.matchscribe_backend.entity.Team;
import com.matchscribe.matchscribe_backend.entity.Venue;

public class MatchDto {
	public Match match;
	public Series series;
	public Team homeTeam;
	public Team awayTeam;
	public Venue venue;
	public List<PlayerDto> homePlayers;
	public List<PlayerDto> awayPlayers;

}
