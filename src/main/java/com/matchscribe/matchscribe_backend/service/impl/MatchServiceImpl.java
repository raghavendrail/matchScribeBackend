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
import com.matchscribe.matchscribe_backend.entity.PlayerCareerBattingStats;
import com.matchscribe.matchscribe_backend.entity.PlayerCareerBowlingStats;
import com.matchscribe.matchscribe_backend.entity.Players;
import com.matchscribe.matchscribe_backend.entity.Series;
import com.matchscribe.matchscribe_backend.entity.Team;
import com.matchscribe.matchscribe_backend.entity.TeamPlayers;
import com.matchscribe.matchscribe_backend.entity.Venue;
import com.matchscribe.matchscribe_backend.entity.enums.MatchResultType;
import com.matchscribe.matchscribe_backend.entity.enums.MatchStatus;
import com.matchscribe.matchscribe_backend.entity.enums.PlayerRoleType;
import com.matchscribe.matchscribe_backend.integration.sportsapi.SportsApiClient;
import com.matchscribe.matchscribe_backend.repository.BattingStatsRepository;
import com.matchscribe.matchscribe_backend.repository.BowlingStatsRepository;
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
	private final BattingStatsRepository battingStatsRepository;
	private final BowlingStatsRepository bowlingStatsRepository;

	public MatchServiceImpl(SportsApiClient sportsApiClient, ObjectMapper objectMapper,
			LeagueRepository leagueRepository, TeamRepository teamRepository, VenueRepository venueRepository,
			MatchRepository matchRepository, SeriesRepository seriesRepository, TeamService teamService,
			TeamPlayersRepository teamPlayersRepository, PlayersRepository playersRepository,
			BattingStatsRepository battingStatsRepository, BowlingStatsRepository bowlingStatsRepository) {
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
		this.battingStatsRepository = battingStatsRepository;
		this.bowlingStatsRepository = bowlingStatsRepository;
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
			processUpcomingMatchPlayers(team1.getSl());
		} catch (Exception ex) {
			result.addWarning("PLAYER_IMPORT_FAILED teamId=" + team1.getTeamId() + " matchId=" + matchId);
		}

		try {
			teamService.getTeamPlayers(team2.getSl(), team2.getTeamId(), matchId);
			processUpcomingMatchPlayers(team2.getSl());
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

	@Override
	@Transactional
	public ImportResult updateRecentMatchesFromDb() {

		ImportResult result = new ImportResult();

		OffsetDateTime fromDate = OffsetDateTime.now().minusDays(7);

		List<Match> matches = matchRepository.findMatchesFromDate(fromDate);

		if (matches.isEmpty()) {
			result.addWarning("NO_MATCHES_FOUND_LAST_7_DAYS");
			return result;
		}

		for (Match match : matches) {

			result.incrementTotal();

			try {
				updateMatchFromMatchCenter(match);
				result.incrementImported();
			} catch (Exception ex) {
				result.incrementSkipped();
				result.addError("MATCH_UPDATE_FAILED [matchId=" + match.getMatchId() + "] " + ex.getMessage());
			}
		}

		return result;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateMatchFromMatchCenter(Match match) throws Exception {

		String json = sportsApiClient.getCompletedMatches(match.getMatchId().intValue());
		JsonNode root = objectMapper.readTree(json);

		// ----------------------------
		// Basic Status
		// ----------------------------
		String state = root.path("state").asText("");
		String status = root.path("status").asText("");

		if ("Complete".equalsIgnoreCase(state)) {
			match.setStatus(MatchStatus.finished.toString());
		} else {
			match.setStatus(MatchStatus.live.toString());
		}

		// ----------------------------
		// Result
		// ----------------------------
		String shortStatus = root.path("shortstatus").asText(null);
		if (shortStatus != null && !shortStatus.isBlank()) {
			match.setResultType(shortStatus);
		}

		// ----------------------------
		// Winner Team
		// ----------------------------
		long winnerApiTeamId = root.path("winningteamid").asLong(0);
		if (winnerApiTeamId > 0) {
			teamRepository.findByTeamId(winnerApiTeamId).ifPresent(t -> match.setWinnerTeamId(t.getSl()));
		}

		// ----------------------------
		// Start / End Date (update safe)
		// ----------------------------
		if (root.has("startdate")) {
			match.setStartDatetime(epochToHuman(root.path("startdate").asLong()));
		}

		if (root.has("enddate")) {
			match.setEndDatetime(epochToHuman(root.path("enddate").asLong()));
		}

		// ----------------------------
		// Store full payload
		// ----------------------------
		match.setExtraInfo(root);

		matchRepository.save(match);
	}

	@Transactional
	public void processUpcomingMatchPlayers(Long teamSl) {

		List<TeamPlayers> teamPlayers = teamPlayersRepository.findByTeamSl(teamSl);

		for (TeamPlayers tp : teamPlayers) {

			Long playerSl = tp.getPlayerSl();
			Optional<Players> optplayer = playersRepository.findBySl(playerSl);
			if (optplayer == null) {
				continue;
			}
			Players player = optplayer.get();
			Long playerId = player.getPlayerId();

			PlayerRoleType roleType = mapRole(tp.getRole());

			if (roleType == null) {
				continue;
			}

			switch (roleType) {

			case BATSMAN:
			case WK_BATSMAN:
				fetchAndSaveBattingStats(playerId);
				break;

			case BOWLER:
				fetchAndSaveBowlingStats(playerId);
				break;

			case BATTING_ALLROUNDER:
			case BOWLING_ALLROUNDER:
				fetchAndSaveBattingStats(playerId);
				fetchAndSaveBowlingStats(playerId);
				break;
			}
		}
	}

	public PlayerRoleType mapRole(String role) {
		if (role == null) {
			return null;
		}

		switch (role.toLowerCase()) {
		case "batsman":
		case "wk-batsman":
			return PlayerRoleType.BATSMAN;

		case "bowler":
			return PlayerRoleType.BOWLER;

		case "batting allrounder":
			return PlayerRoleType.BATTING_ALLROUNDER;

		case "bowling allrounder":
			return PlayerRoleType.BOWLING_ALLROUNDER;

		default:
			return null;
		}
	}

	public void fetchAndSaveBattingStats(Long playerId) {

		String json = sportsApiClient.getPlayerBattingStats(playerId.intValue());

		if (json == null || json.isEmpty()) {
			return;
		}
		try {

			JsonNode root = objectMapper.readTree(json);

			JsonNode headers = root.get("headers"); // Test, ODI, T20, IPL
			JsonNode values = root.get("values");

			for (int col = 1; col < headers.size(); col++) {

				String matchType = headers.get(col).asText();

				PlayerCareerBattingStats stats = parseBattingStats(values, col);

				// skip empty stats
				if (stats == null) {
					continue;
				}
				Optional<Players> playerOpt = playersRepository.findByPlayerId(playerId);
				stats.setPlayerSl(playerOpt.get().getSl());
				stats.setMatchType(matchType);
				if (battingStatsRepository.existsByPlayerSlAndMatchType(playerOpt.get().getSl(), matchType)) {
					continue;
				}

				battingStatsRepository.save(stats);
			}
		} catch (Exception ex) {
			// Log or handle exception as needed
			ex.printStackTrace();
		}
	}

	public void fetchAndSaveBowlingStats(Long playerId) {

		String json = sportsApiClient.getPlayerBowlingStats(playerId.intValue());

		if (json == null || json.isEmpty()) {
			return;
		}
		try {

			JsonNode root = objectMapper.readTree(json);

			JsonNode headers = root.get("headers");
			JsonNode values = root.get("values");

			for (int col = 1; col < headers.size(); col++) {

				String matchType = headers.get(col).asText();

				PlayerCareerBowlingStats stats = parseBowlingStats(values, col);

				if (stats == null) {
					continue;
				}
				Optional<Players> player = playersRepository.findByPlayerId(playerId);
				stats.setPlayerSl(player.get().getSl());
				stats.setMatchType(matchType);
				if (bowlingStatsRepository.existsByPlayerSlAndMatchType(player.get().getSl(), matchType)) {
					continue;
				}
				bowlingStatsRepository.save(stats);
			}
		} catch (Exception ex) {
			// Log or handle exception as needed
			ex.printStackTrace();
		}
	}

	private PlayerCareerBattingStats parseBattingStats(JsonNode values, int col) {

		PlayerCareerBattingStats s = new PlayerCareerBattingStats();

		for (JsonNode row : values) {

			String key = row.get("values").get(0).asText();
			String val = row.get("values").get(col).asText();

			switch (key) {
			case "Matches":
				s.setMatches(parseInt(val));
				break;
			case "Innings":
				s.setInnings(parseInt(val));
				break;
			case "Runs":
				s.setRuns(parseInt(val));
				break;
			case "Balls":
				s.setBalls(parseInt(val));
				break;
			case "Highest":
				s.setHighestScore(parseInt(val));
				break;
			case "Average":
				s.setAverage(parseDouble(val));
				break;
			case "SR":
				s.setStrikeRate(parseDouble(val));
				break;
			case "Not Out":
				s.setNotOuts(parseInt(val));
				break;
			case "Fours":
				s.setFours(parseInt(val));
				break;
			case "Sixes":
				s.setSixes(parseInt(val));
				break;
			}
		}
		return s;
	}

	private PlayerCareerBowlingStats parseBowlingStats(JsonNode values, int col) {

		PlayerCareerBowlingStats s = new PlayerCareerBowlingStats();

		for (JsonNode row : values) {

			String key = row.get("values").get(0).asText();
			String val = row.get("values").get(col).asText();

			switch (key) {
			case "Matches":
				s.setMatches(parseInt(val));
				break;
			case "Innings":
				s.setInnings(parseInt(val));
				break;
			case "Balls":
				s.setBalls(parseInt(val));
				break;
			case "Runs":
				s.setRuns(parseInt(val));
				break;
			case "Maidens":
				s.setMaidens(parseInt(val));
				break;
			case "Wickets":
				s.setWickets(parseInt(val));
				break;
			case "Avg":
				s.setAverage(parseDouble(val));
				break;
			case "Eco":
				s.setEconomy(parseDouble(val));
				break;
			case "SR":
				s.setStrikeRate(parseDouble(val));
				break;
			case "BBI":
				s.setBestBowlingInnings(val);
				break;
			case "BBM":
				s.setBestBowlingMatch(val);
				break;
			}
		}
		return s;
	}

	private Integer parseInt(String val) {
		if (val == null || val.equals("-") || val.equals("-/-")) {
			return 0;
		}
		return Integer.parseInt(val);
	}

	private Double parseDouble(String val) {
		if (val == null || val.equals("-") || val.equals("-/-")) {
			return 0.0;
		}
		return Double.parseDouble(val);
	}

}
