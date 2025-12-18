package com.matchscribe.matchscribe_backend.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matchscribe.matchscribe_backend.dto.team.PlayerDto;
import com.matchscribe.matchscribe_backend.entity.Players;
import com.matchscribe.matchscribe_backend.entity.TeamPlayers;
import com.matchscribe.matchscribe_backend.repository.BattingStatsRepository;
import com.matchscribe.matchscribe_backend.repository.BowlingStatsRepository;
import com.matchscribe.matchscribe_backend.repository.PlayersRepository;
import com.matchscribe.matchscribe_backend.repository.TeamPlayersRepository;
import com.matchscribe.matchscribe_backend.service.PlayersService;

@Service
@Transactional(readOnly = true)
public class PlayersServiceImpl implements PlayersService {

	private final PlayersRepository playersRepository;
	private final BattingStatsRepository battingStatsRepository;
	private final BowlingStatsRepository bowlingStatsRepository;
	private final TeamPlayersRepository teamPlayersRepository;

	public PlayersServiceImpl(PlayersRepository playersRepository, BattingStatsRepository battingStatsRepository,
			BowlingStatsRepository bowlingStatsRepository, TeamPlayersRepository teamPlayersRepository) {

		this.playersRepository = playersRepository;
		this.battingStatsRepository = battingStatsRepository;
		this.bowlingStatsRepository = bowlingStatsRepository;
		this.teamPlayersRepository = teamPlayersRepository;
	}

	@Override
	public PlayerDto getPlayerById(Long playerId) {

		Optional<Players> optPlayer = playersRepository.findByPlayerId(playerId);
		if (optPlayer.isEmpty()) {
			return null;
		}

		Players player = optPlayer.get();
		TeamPlayers teamPlayer = teamPlayersRepository.findByPlayerSl(player.getSl());

		PlayerDto dto = new PlayerDto();
		dto.sl = player.getSl();
		dto.playerId = player.getPlayerId();
		dto.name = player.getName();
		dto.battingStyle = player.getBattingStyle();
		dto.bowlingStyle = player.getBowlingStyle();
		dto.role = teamPlayer.getRole();

		// Career stats (by DB player SL)
		dto.careerBattingStats = battingStatsRepository.findByPlayerSl(player.getSl());

		dto.careerBowlingStats = bowlingStatsRepository.findByPlayerSl(player.getSl());

		return dto;
	}
}
