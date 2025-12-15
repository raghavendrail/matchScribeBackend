package com.matchscribe.matchscribe_backend.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matchscribe.matchscribe_backend.dto.match.MatchDto;
import com.matchscribe.matchscribe_backend.dto.team.PlayerDto;
import com.matchscribe.matchscribe_backend.entity.League;
import com.matchscribe.matchscribe_backend.entity.Match;
import com.matchscribe.matchscribe_backend.entity.Players;
import com.matchscribe.matchscribe_backend.entity.Series;
import com.matchscribe.matchscribe_backend.entity.Team;
import com.matchscribe.matchscribe_backend.entity.TeamPlayers;
import com.matchscribe.matchscribe_backend.entity.Venue;
import com.matchscribe.matchscribe_backend.entity.enums.MatchResultType;
import com.matchscribe.matchscribe_backend.entity.enums.MatchStatus;
import com.matchscribe.matchscribe_backend.integration.sportsapi.SportsApiClient;
import com.matchscribe.matchscribe_backend.repository.LeagueRepository;
import com.matchscribe.matchscribe_backend.repository.MatchRepository;
import com.matchscribe.matchscribe_backend.repository.PlayersRepository;
import com.matchscribe.matchscribe_backend.repository.SeriesRepository;
import com.matchscribe.matchscribe_backend.repository.TeamPlayersRepository;
import com.matchscribe.matchscribe_backend.repository.TeamRepository;
import com.matchscribe.matchscribe_backend.repository.VenueRepository;
import com.matchscribe.matchscribe_backend.service.MatchService;
import com.matchscribe.matchscribe_backend.service.TeamService;
import com.matchscribe.matchscribe_backend.util.ImportResult;
import com.matchscribe.matchscribe_backend.util.SlugUtil;

@Service
public class MatchServiceImpl implements MatchService {

	private final SportsApiClient sportsApiClient;
	private final ObjectMapper objectMapper;

	private final LeagueRepository leagueRepository;
	private final TeamRepository teamRepository;
	private final VenueRepository venueRepository;
	private final MatchRepository matchRepository;
	private final SeriesRepository seriesRepository;
	private final TeamService teamService;
	private final TeamPlayersRepository teamPlayersRepository;
	private final PlayersRepository playersRepository;

	public MatchServiceImpl(SportsApiClient sportsApiClient, ObjectMapper objectMapper,
			LeagueRepository leagueRepository, TeamRepository teamRepository, VenueRepository venueRepository,
			MatchRepository matchRepository, SeriesRepository seriesRepository, TeamService teamService,
			TeamPlayersRepository teamPlayersRepository, PlayersRepository playersRepository) {
		this.sportsApiClient = sportsApiClient;
		this.objectMapper = objectMapper;
		this.leagueRepository = leagueRepository;
		this.teamRepository = teamRepository;
		this.venueRepository = venueRepository;
		this.matchRepository = matchRepository;
		this.seriesRepository = seriesRepository;
		this.teamService = teamService;
		this.teamPlayersRepository = teamPlayersRepository;
		this.playersRepository = playersRepository;
	}

	@Override
	public ImportResult importUpcomingMatchesFromSportsApi() {

		ImportResult result = new ImportResult();

		String json;
		try {
			json = sportsApiClient.getUpcomingMatches();
		} catch (Exception ex) {
			result.addError("API_FETCH_FAILED: " + ex.getMessage());
			return result;
		}

		try {
			JsonNode root = objectMapper.readTree(json);
			JsonNode typeMatches = root.path("typeMatches");

			if (!typeMatches.isArray()) {
				result.addError("INVALID_RESPONSE: typeMatches missing");
				return result;
			}

			for (JsonNode typeMatchNode : typeMatches) {

				String matchType = typeMatchNode.path("matchType").asText(null);
				if (matchType == null || matchType.isBlank()) {
					continue;
				}

				League league = getOrCreateLeague(matchType, result);
				if (league == null) {
					continue;
				}

				JsonNode seriesMatches = typeMatchNode.path("seriesMatches");
				if (!seriesMatches.isArray()) {
					continue;
				}

				for (JsonNode seriesMatch : seriesMatches) {

					JsonNode wrapper = seriesMatch.path("seriesAdWrapper");
					long seriesId = wrapper.path("seriesId").asLong(-1);
					if (seriesId == -1) {
						continue;
					}

					Series series = getOrCreateSeries(wrapper, league, result);
					if (series == null) {
						continue;
					}

					JsonNode matches = wrapper.path("matches");
					if (!matches.isArray()) {
						continue;
					}

					for (JsonNode matchNode : matches) {
						result.incrementTotal();

						try {
							saveMatchInNewTransaction(matchNode, league, series, result);

							result.incrementImported();
						} catch (Exception ex) {
							long matchId = matchNode.path("matchInfo").path("matchId").asLong();
							result.incrementSkipped();
							result.addError("MATCH_FAILED [matchId=" + matchId + "]: " + ex.getMessage());
						}
					}
				}
			}

		} catch (Exception ex) {
			result.addError("JSON_PARSE_FAILED: " + ex.getMessage());
		}

		return result;
	}

	// ----------------------------------------------------
	// PROCESS SINGLE MATCH
	// ----------------------------------------------------
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveMatchInNewTransaction(JsonNode matchNode, League league, Series series, ImportResult result) {

		processSingleMatch(matchNode, league, series, result);
	}

	private void processSingleMatch(JsonNode matchNode, League league, Series series, ImportResult result) {

		JsonNode info = matchNode.path("matchInfo");
		long matchId = info.path("matchId").asLong();

		if (matchRepository.findByMatchId(matchId).isPresent()) {
			return;
		}

		Venue venue = getOrCreateVenue(info.path("venueInfo"));
		Team team1 = getOrCreateTeam(info.path("team1"), league.getSl());
		Team team2 = getOrCreateTeam(info.path("team2"), league.getSl());

		// 🔹 Player import is OPTIONAL
		try {
			teamService.getTeamPlayers(team1.getSl(), team1.getTeamId(), matchId);
		} catch (Exception ex) {
			result.addWarning("PLAYER_IMPORT_FAILED teamId=" + team1.getTeamId() + " matchId=" + matchId);
		}

		try {
			teamService.getTeamPlayers(team2.getSl(), team2.getTeamId(), matchId);
		} catch (Exception ex) {
			result.addWarning("PLAYER_IMPORT_FAILED teamId=" + team2.getTeamId() + " matchId=" + matchId);
		}

		// 🔹 ALWAYS save match
		Match match = new Match();
		match.setSportId(1L);
		match.setSeriesId(series.getSl());
		match.setVenueId(venue.getSl());
		match.setHomeTeamId(team1.getSl());
		match.setAwayTeamId(team2.getSl());
		match.setStartDatetime(epochToHuman(info.path("startDate").asLong()));
		match.setEndDatetime(epochToHuman(info.path("endDate").asLong()));
		match.setMatchDesc(matchNode.path("matchDesc").asText());
		match.setMatchId(matchId);
		match.setStatus(MatchStatus.scheduled.toString());
		match.setResultType(MatchResultType.unknown.toString());
		match.setSlug(SlugUtil
				.toSlug(team1.getTeamName() + " vs " + team2.getTeamName() + " " + info.path("startDate").asText()));
		matchRepository.saveAndFlush(match);
	}

	// ----------------------------------------------------
	// HELPERS
	// ----------------------------------------------------
	private League getOrCreateLeague(String name, ImportResult result) {
		try {
			return leagueRepository.findByName(name).orElseGet(() -> {
				League l = new League();
				l.setName(name);
				l.setSportId(1L);
				l.setSlug(SlugUtil.toSlug(name));
				return leagueRepository.saveAndFlush(l);
			});
		} catch (Exception ex) {
			result.addError("LEAGUE_SAVE_FAILED [" + name + "]");
			return null;
		}
	}

	private Series getOrCreateSeries(JsonNode wrapper, League league, ImportResult result) {
		long seriesId = wrapper.path("seriesId").asLong();

		try {
			return seriesRepository.findBySeriesId(seriesId).orElseGet(() -> {
				Series s = new Series();
				s.setSeriesId(seriesId);
				s.setSeriesName(wrapper.path("seriesName").asText());
				s.setLeagueSl(league.getSl());
				s.setInsertBy(LocalDateTime.now());
				s.setSlug(SlugUtil.toSlug(wrapper.path("seriesName").asText()));
				return seriesRepository.saveAndFlush(s);
			});
		} catch (Exception ex) {
			result.addError("SERIES_SAVE_FAILED [seriesId=" + seriesId + "]");
			return null;
		}
	}

	private Venue getOrCreateVenue(JsonNode venueNode) {
		long venueId = venueNode.path("id").asLong();

		return venueRepository.findByVenueId(venueId).orElseGet(() -> {
			Venue v = new Venue();
			v.setVenueId(venueId);
			v.setGround(venueNode.path("ground").asText());
			v.setCity(venueNode.path("city").asText());
			v.setCountry(venueNode.path("country").asText());
			v.setSlug(SlugUtil.toSlug(venueNode.path("ground").asText() + "-" + venueNode.path("city").asText()));
			return venueRepository.saveAndFlush(v);
		});
	}

	private Team getOrCreateTeam(JsonNode teamNode, Long leagueSl) {
		long teamId = teamNode.path("teamId").asLong();

		return teamRepository.findByTeamId(teamId).orElseGet(() -> {
			Team t = new Team();
			t.setLeagueSl(leagueSl);
			t.setTeamId(teamId);
			t.setTeamName(teamNode.path("teamName").asText());
			t.setTeamSName(teamNode.path("teamSName").asText());
			t.setSlug(SlugUtil.toSlug(teamNode.path("teamName").asText()));
			return teamRepository.saveAndFlush(t);
		});
	}

	@Override
	public MatchDto getMatchBySlug(String slug) {
		Optional<Match> optMatch = matchRepository.findBySlug(slug);
		if (!optMatch.isPresent()) {
			return null;
		}

		Match match = optMatch.get();
		MatchDto dto = new MatchDto();
		dto.match = match;

		// series (note: your Match entity stores SeriesId as Long)
		Long seriesId = match.getSeriesId();
		if (seriesId != null) {
			Optional<Series> optSeries = seriesRepository.findBySl(seriesId);
			optSeries.ifPresent(s -> dto.series = s);
		}

		if (match.getHomeTeamId() != null) {
			teamRepository.findBySl(match.getHomeTeamId()).ifPresent(t -> dto.homeTeam = t);

			dto.homePlayers = loadTeamPlayers(match.getHomeTeamId());
		}

		// ------------------------
		// Away Team
		// ------------------------
		if (match.getAwayTeamId() != null) {
			teamRepository.findBySl(match.getAwayTeamId()).ifPresent(t -> dto.awayTeam = t);

			dto.awayPlayers = loadTeamPlayers(match.getAwayTeamId());
		}

		// venue
		Long venueId = match.getVenueId();
		if (venueId != null) {
			Optional<Venue> optVenue = venueRepository.findBySl(venueId);
			optVenue.ifPresent(v -> dto.venue = v);
		}

		return dto;
	}

	public static OffsetDateTime epochToHuman(long epochMillis) {
		Instant instant = Instant.ofEpochMilli(epochMillis);
		ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
		return zdt.toOffsetDateTime();
	}

	@Override
	@Transactional(readOnly = true)
	public MatchDto getMatchById(Long matchId) {
		Optional<Match> optMatch = matchRepository.findByMatchId(matchId);
		if (!optMatch.isPresent()) {
			return null;
		}

		Match match = optMatch.get();
		MatchDto dto = new MatchDto();
		dto.match = match;

		// series (note: your Match entity stores SeriesId as Long)
		Long seriesId = match.getSeriesId();
		if (seriesId != null) {
			Optional<Series> optSeries = seriesRepository.findBySl(seriesId);
			optSeries.ifPresent(s -> dto.series = s);
		}

		// teams
		Long homeTeamId = match.getHomeTeamId();
		if (homeTeamId != null) {
			Optional<Team> optHome = teamRepository.findBySl(homeTeamId);
			optHome.ifPresent(t -> dto.homeTeam = t);
		}

		// away team
		Long awayTeamId = match.getAwayTeamId();
		if (awayTeamId != null) {
			Optional<Team> optAway = teamRepository.findBySl(awayTeamId);
			optAway.ifPresent(t -> dto.awayTeam = t);
		}

		// venue
		Long venueId = match.getVenueId();
		if (venueId != null) {
			Optional<Venue> optVenue = venueRepository.findBySl(venueId);
			optVenue.ifPresent(v -> dto.venue = v);
		}

		return dto;
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<MatchDto> getAllMatches() {
		Iterable<Match> matches = matchRepository.findAll();
		List<MatchDto> dtos = new ArrayList<>();
		for (Match match : matches) {
			MatchDto dto = new MatchDto();
			dto.match = match;

			// series
			Long seriesId = match.getSeriesId();
			if (seriesId != null) {
				Optional<Series> optSeries = seriesRepository.findBySl(seriesId);
				optSeries.ifPresent(s -> dto.series = s);
			}

			// home team
			Long homeTeamId = match.getHomeTeamId();
			if (homeTeamId != null) {
				Optional<Team> optHome = teamRepository.findBySl(homeTeamId);
				optHome.ifPresent(t -> dto.homeTeam = t);
			}

			// away team
			Long awayTeamId = match.getAwayTeamId();
			if (awayTeamId != null) {
				Optional<Team> optAway = teamRepository.findBySl(awayTeamId);
				optAway.ifPresent(t -> dto.awayTeam = t);
			}

			// venue
			Long venueId = match.getVenueId();
			if (venueId != null) {
				Optional<Venue> optVenue = venueRepository.findBySl(venueId);
				optVenue.ifPresent(v -> dto.venue = v);
			}

			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public List<MatchDto> getUpcomingMatches() {

		List<Match> matches = matchRepository.findByStatus(MatchStatus.scheduled.toString());
		List<MatchDto> dtos = new ArrayList<>();

		for (Match match : matches) {
			MatchDto dto = new MatchDto();
			dto.match = match;

			// Series
			if (match.getSeriesId() != null) {
				seriesRepository.findBySl(match.getSeriesId()).ifPresent(series -> dto.series = series);
			}

			// Home Team
			if (match.getHomeTeamId() != null) {
				teamRepository.findBySl(match.getHomeTeamId()).ifPresent(team -> dto.homeTeam = team);
			}

			// Away Team
			if (match.getAwayTeamId() != null) {
				teamRepository.findBySl(match.getAwayTeamId()).ifPresent(team -> dto.awayTeam = team);
			}

			// Venue
			if (match.getVenueId() != null) {
				venueRepository.findBySl(match.getVenueId()).ifPresent(venue -> dto.venue = venue);
			}

			dtos.add(dto);
		}

		return dtos;
	}

	private List<PlayerDto> loadTeamPlayers(Long teamSl) {

		List<TeamPlayers> mappings = teamPlayersRepository.findByTeamSl(teamSl);

		if (mappings.isEmpty()) {
			return List.of();
		}

		// Extract player SLs
		List<Long> playerSls = mappings.stream().map(TeamPlayers::getPlayerSl).toList();

		// Fetch players
		Map<Long, Players> playerMap = playersRepository.findBySlIn(playerSls).stream()
				.collect(Collectors.toMap(Players::getSl, p -> p));

		// Build DTO
		return mappings.stream().map(tp -> {
			Players p = playerMap.get(tp.getPlayerSl());

			PlayerDto dto = new PlayerDto();
			dto.sl = p.getSl();
			dto.playerId = p.getPlayerId();
			dto.name = p.getName();
			dto.battingStyle = p.getBattingStyle();
			dto.bowlingStyle = p.getBowlingStyle();
			dto.role = tp.getRole();

			return dto;
		}).toList();
	}

}
