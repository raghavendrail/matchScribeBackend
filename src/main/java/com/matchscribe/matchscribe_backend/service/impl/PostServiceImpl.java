package com.matchscribe.matchscribe_backend.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matchscribe.matchscribe_backend.dto.post.PostDto;
import com.matchscribe.matchscribe_backend.dto.team.PlayerDto;
import com.matchscribe.matchscribe_backend.entity.Players;
import com.matchscribe.matchscribe_backend.entity.Post;
import com.matchscribe.matchscribe_backend.repository.PlayersRepository;
import com.matchscribe.matchscribe_backend.repository.PostRepository;
import com.matchscribe.matchscribe_backend.service.PostService;

@Service
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;
	private final PlayersRepository playersRepository;

	public PostServiceImpl(PostRepository postRepository, PlayersRepository playersRepository) {
		this.postRepository = postRepository;
		this.playersRepository = playersRepository;
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
			return dto;
		}).filter(Objects::nonNull).toList();
	}
}
