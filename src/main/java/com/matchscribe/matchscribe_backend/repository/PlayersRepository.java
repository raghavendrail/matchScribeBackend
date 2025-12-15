package com.matchscribe.matchscribe_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matchscribe.matchscribe_backend.entity.Players;

public interface PlayersRepository extends JpaRepository<Players, Long> {
	Optional<Players> findByPlayerId(Long playerId);

	List<Players> findBySlIn(List<Long> sls);

}
