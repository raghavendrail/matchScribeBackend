package com.matchscribe.matchscribe_backend.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matchscribe.matchscribe_backend.entity.Players;
import com.matchscribe.matchscribe_backend.entity.Team;
import com.matchscribe.matchscribe_backend.entity.TeamPlayers;
import com.matchscribe.matchscribe_backend.integration.sportsapi.SportsApiClient;
import com.matchscribe.matchscribe_backend.repository.PlayersRepository;
import com.matchscribe.matchscribe_backend.repository.TeamPlayersRepository;
import com.matchscribe.matchscribe_backend.repository.TeamRepository;
import com.matchscribe.matchscribe_backend.service.TeamService;

@Service
@Transactional
public class TeamServiceImpl implements TeamService {

	private final SportsApiClient sportsApiClient;
	private final ObjectMapper objectMapper;
	private final PlayersRepository playersRepository;
	private final TeamPlayersRepository teamPlayersRepository;
	private final TeamRepository teamRepository;

	public TeamServiceImpl(SportsApiClient sportsApiClient, ObjectMapper objectMapper,
			PlayersRepository playersRepository, TeamPlayersRepository teamPlayersRepository,
			TeamRepository teamRepository) {
		this.sportsApiClient = sportsApiClient;
		this.objectMapper = objectMapper;
		this.playersRepository = playersRepository;
		this.teamPlayersRepository = teamPlayersRepository;
		this.teamRepository = teamRepository;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void importTeamPlayers(Long teamSl, Long teamId) {

		try {
			String json = sportsApiClient.getTeamPlayersRaw(teamId.intValue());
			JsonNode root = objectMapper.readTree(json);
			JsonNode playersArray = root.path("player");

			if (!playersArray.isArray()) {
				return;
			}

			Team team = teamRepository.findById(teamSl)
					.orElseThrow(() -> new IllegalStateException("Team not found: " + teamSl));

			String currentRole = null;

			for (JsonNode playerNode : playersArray) {

				// 🔹 Role header (BATSMEN / BOWLER / etc.)
				if (!playerNode.has("id")) {
					currentRole = normalizeRole(playerNode.path("name").asText());
					continue;
				}

				Long apiPlayerId = playerNode.path("id").asLong();

				Players player = playersRepository.findByPlayerId(apiPlayerId).orElseGet(() -> {
					Players p = new Players();
					p.setPlayerId(apiPlayerId);
					return p;
				});

				// update fields (safe for re-import)
				player.setName(playerNode.path("name").asText());
				player.setBattingStyle(playerNode.path("battingStyle").asText(null));
				player.setBowlingStyle(playerNode.path("bowlingStyle").asText(null));

				player = playersRepository.save(player);

				// mapping table
				Optional<TeamPlayers> mappingExists = teamPlayersRepository.findByTeamSlAndPlayerSl(teamSl,
						player.getSl());

				if (mappingExists.isEmpty()) {
					TeamPlayers tp = new TeamPlayers();
					tp.setTeamSl(team.getSl());
					tp.setPlayerSl(player.getSl());
					tp.setRole(currentRole);
					tp.setIsActive(true);
					teamPlayersRepository.save(tp);
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("Failed to import team players for teamId=" + teamId, e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void getTeamPlayers(Long teamSl, Long teamId, Long matchId) {

		try {
			String json = sportsApiClient.getTeamInfoRow(matchId.intValue(), teamId.intValue());

			JsonNode root = objectMapper.readTree(json);
			JsonNode categories = root.path("player");

			if (!categories.isArray()) {
				return;
			}

			// 🔹 Step 1: check if Playing XI exists
			boolean hasPlayingXI = false;
			for (JsonNode c : categories) {
				if ("playing XI".equalsIgnoreCase(c.path("category").asText())) {
					hasPlayingXI = true;
					break;
				}
			}

			// 🔹 Step 2: choose category
			String selectedCategory = hasPlayingXI ? "playing XI" : "Squad";

			// 🔹 Step 3: process players
			for (JsonNode categoryNode : categories) {

				String category = categoryNode.path("category").asText("");
				if (!selectedCategory.equalsIgnoreCase(category)) {
					continue;
				}

				JsonNode playersArray = categoryNode.path("player");
				if (!playersArray.isArray()) {
					continue;
				}

				for (JsonNode p : playersArray) {

					long apiPlayerId = p.path("id").asLong();
					String name = p.path("name").asText();
					String role = p.path("role").asText(null);
					String battingStyle = p.path("battingStyle").asText(null);
					String bowlingStyle = p.path("bowlingStyle").asText(null);

					// 🔹 Save / fetch player
					Players player = playersRepository.findByPlayerId(apiPlayerId).orElseGet(() -> {
						Players np = new Players();
						np.setPlayerId(apiPlayerId);
						np.setName(name);
						np.setBattingStyle(battingStyle);
						np.setBowlingStyle(bowlingStyle);
						return playersRepository.save(np);
					});

					// 🔹 Save team-player mapping
					teamPlayersRepository.findByTeamSlAndPlayerSl(teamSl, player.getSl()).orElseGet(() -> {
						TeamPlayers tp = new TeamPlayers();
						tp.setTeamSl(teamSl);
						tp.setPlayerSl(player.getSl());
						tp.setRole(role);
						tp.setIsActive(true);
						return teamPlayersRepository.save(tp);
					});
				}

				// once processed, break (important)
				break;
			}

		} catch (Exception ex) {
			// ❌ DO NOT FAIL MATCH SAVE
			System.err.println(
					"Team player import failed | teamId=" + teamId + ", matchId=" + matchId + " | " + ex.getMessage());
		}
	}

	/**
	 * Normalize role names from API
	 */
	private String normalizeRole(String apiRole) {
		if (apiRole == null) {
			return null;
		}

		return switch (apiRole.toUpperCase()) {
		case "BATSMEN" -> "BATSMAN";
		case "ALL ROUNDER" -> "ALL_ROUNDER";
		case "WICKET KEEPER" -> "WICKET_KEEPER";
		case "BOWLER" -> "BOWLER";
		default -> apiRole.replace(" ", "_").toUpperCase();
		};
	}
}
