package com.matchscribe.matchscribe_backend.dto.post;

import java.util.List;

import com.matchscribe.matchscribe_backend.dto.team.PlayerDto;
import com.matchscribe.matchscribe_backend.entity.Post;

public class PostDto {
	public Post post;
	public List<PlayerDto> homePlayers;
	public List<PlayerDto> awayPlayers;

}
