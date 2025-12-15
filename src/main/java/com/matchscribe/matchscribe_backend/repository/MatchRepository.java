package com.matchscribe.matchscribe_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matchscribe.matchscribe_backend.entity.Match;

public interface MatchRepository extends JpaRepository<Match, Long> {
	boolean existsByMatchId(Long matchId);

	Optional<Match> findByMatchId(Long matchId);

	List<Match> findByStatus(String status);

	Optional<Match> findBySlug(String slug);
}
