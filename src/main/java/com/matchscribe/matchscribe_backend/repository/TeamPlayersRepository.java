package com.matchscribe.matchscribe_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matchscribe.matchscribe_backend.entity.TeamPlayers;

public interface TeamPlayersRepository extends JpaRepository<TeamPlayers, Long> {
	Optional<TeamPlayers> findByTeamSlAndPlayerSl(Long teamSl, Long playerSl);

	List<TeamPlayers> findByTeamSl(Long teamSl);

	TeamPlayers findByPlayerSl(Long sl);

}
