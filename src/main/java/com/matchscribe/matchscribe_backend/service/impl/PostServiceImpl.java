package com.matchscribe.matchscribe_backend.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matchscribe.matchscribe_backend.dto.match.MatchDto;
import com.matchscribe.matchscribe_backend.dto.post.PostDto;
import com.matchscribe.matchscribe_backend.dto.tag.TagDto;
import com.matchscribe.matchscribe_backend.dto.team.PlayerDto;
import com.matchscribe.matchscribe_backend.entity.Players;
import com.matchscribe.matchscribe_backend.entity.Post;
import com.matchscribe.matchscribe_backend.entity.TeamPlayers;
import com.matchscribe.matchscribe_backend.repository.PlayersRepository;
import com.matchscribe.matchscribe_backend.repository.PostRepository;
import com.matchscribe.matchscribe_backend.repository.SeriesRepository;
import com.matchscribe.matchscribe_backend.repository.TeamPlayersRepository;
import com.matchscribe.matchscribe_backend.repository.TeamRepository;
import com.matchscribe.matchscribe_backend.service.MatchService;
import com.matchscribe.matchscribe_backend.service.OpenApiService;
import com.matchscribe.matchscribe_backend.service.PostService;
import com.matchscribe.matchscribe_backend.service.PostTagService;
import com.matchscribe.matchscribe_backend.util.ImportResult;

@Service
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;
	private final PlayersRepository playersRepository;
	private final TeamPlayersRepository teamPlayersRepository;
	private final MatchService matchService;
	private final OpenApiService openApiService;
	private final PostTagService postTagService;
	private final TeamRepository teamRepoitory;
	private final SeriesRepository seriesRepository;

	public PostServiceImpl(PostRepository postRepository, PlayersRepository playersRepository,
			TeamPlayersRepository teamPlayersRepository, MatchService matchService, OpenApiService openApiService,
			PostTagService postTagService, TeamRepository teamRepoitory, SeriesRepository seriesRepository) {
		this.postRepository = postRepository;
		this.playersRepository = playersRepository;
		this.teamPlayersRepository = teamPlayersRepository;
		this.matchService = matchService;
		this.openApiService = openApiService;
		this.postTagService = postTagService;
		this.teamRepoitory = teamRepoitory;
		this.seriesRepository = seriesRepository;
	}

	@Override
	public PostDto getPostBySlug(String slug) {

		Optional<Post> optPost = postRepository.findBySlug(slug);
		if (optPost.isEmpty()) {
			return null;
		}

		Post post = optPost.get();

		PostDto dto = new PostDto();
		dto.post = post;

		// -----------------------------
		// Home Probable XI
		// -----------------------------
		dto.homePlayers = loadPlayers(post.getHomeProbableXi());

		// -----------------------------
		// Away Probable XI
		// -----------------------------
		dto.awayPlayers = loadPlayers(post.getAwayProbableXi());

		return dto;
	}

	// ------------------------------------------------
	// Helper: Resolve player SLs to PlayerDto list
	// ------------------------------------------------
	private List<PlayerDto> loadPlayers(Integer[] playerSls) {

		if (playerSls == null || playerSls.length == 0) {
			return List.of();
		}

		List<Long> sls = Arrays.stream(playerSls).filter(Objects::nonNull).map(Long::valueOf).toList();

		Map<Long, Players> playerMap = playersRepository.findBySlIn(sls).stream()
				.collect(Collectors.toMap(Players::getSl, p -> p));

		return sls.stream().map(sl -> {
			Players p = playerMap.get(sl);
			if (p == null) {
				return null;
			}

			PlayerDto dto = new PlayerDto();
			dto.sl = p.getSl();
			dto.playerId = p.getPlayerId();
			dto.name = p.getName();
			dto.battingStyle = p.getBattingStyle();
			dto.bowlingStyle = p.getBowlingStyle();
			TeamPlayers teamPlayers = teamPlayersRepository.findByPlayerSl(p.getSl());
			dto.role = teamPlayers.getRole();
			return dto;
		}).filter(Objects::nonNull).toList();
	}

	@Override
	public ImportResult generatePostsForUpcomingMatches() {

		ImportResult result = new ImportResult();
		List<MatchDto> matches = matchService.getUpcomingMatches();

		for (MatchDto matchDto : matches) {

			result.incrementTotal();

			String matchSlug = matchDto.match.getSlug();

			try {
				Optional<Post> optPost = postRepository.findBySlug(matchSlug);

				if (optPost.isPresent()) {
					result.incrementSkipped();
					result.addWarning("Post already exists for slug: " + matchSlug);
					continue;
				}

				String matchContext = matchDto.toString();

				Post post = new Post();
				post.setSportId(matchDto.match.getSportId());
				post.setMatchId(matchDto.match.getMatchId());
				post.setLeagueId(matchDto.series.getLeagueSl());
				post.setSlug(matchSlug);

				// AI generated content
				post.setTitle(openApiService.generateTitle(matchContext));
				post.setSeoTitle(openApiService.generateSeoTitle(matchContext));
				post.setTagSlugs(openApiService.generateTagSlug(matchContext));
				post.setSeoDescription(openApiService.generateSeoDescription(matchContext));

				post.setOverview(openApiService.generateOverview(matchContext));
				post.setTeamForm(openApiService.generateTeamForm(matchContext));
				post.setPitchReport(openApiService.generatePitchReport(matchContext));
				post.setWeather(openApiService.generateWeatherReport(matchContext));
				post.setKeyPlayers(openApiService.generateKeyPlayers(matchContext));
				post.setHeadToHead(openApiService.generateHeadToHead(matchContext));

				post.setHomeProbableXi(openApiService.generateProbableXi(matchContext, "home"));
				post.setAwayProbableXi(openApiService.generateProbableXi(matchContext, "away"));

				post.setFantasyTips(openApiService.generateFantasyTips(matchContext));
				post.setFinalVerdict(openApiService.generateFinalVerdict(matchContext));

				postRepository.save(post);

				// -----------------------------
				// TAG GENERATION + SYNC (🔥 THIS IS THE PLACE 🔥)
				// -----------------------------

				List<TagDto> tagInputs = new ArrayList<>();

				// 1️⃣ AI-generated tags

				String[] aiTags = openApiService.generateTagSlug(matchContext);

				if (aiTags != null) {
					for (String tag : aiTags) {
						tagInputs.add(new TagDto(tag, "AI"));
					}
				}
				String homeTeamName = teamRepoitory.findNameBySl(matchDto.match.getHomeTeamId());
				String awayTeamName = teamRepoitory.findNameBySl(matchDto.match.getAwayTeamId());

				if (homeTeamName == null) {
					homeTeamName = "Unknown Team";
				}
				if (awayTeamName == null) {
					awayTeamName = "Unknown Team";
				}

				// 2️⃣ Match-based auto tags
				tagInputs.add(new TagDto(homeTeamName + " vs " + awayTeamName, "MATCH"));
				String seriesSlug = seriesRepository.findNameBySl(matchDto.match.getSeriesId());

				tagInputs.add(new TagDto(seriesSlug, "FORMAT"));

				tagInputs.add(new TagDto(matchDto.venue.getCity(), "VENUE"));

				// 3️⃣ Sync to DB (THIS CALL DOES EVERYTHING)
				postTagService.syncPostTags(post.getId(), tagInputs);

				result.incrementImported();

			} catch (Exception ex) {
				result.addError("Failed to generate post for matchSlug=" + matchSlug + " | Error: " + ex.getMessage());
			}
		}

		return result;
	}

}
