package com.matchscribe.matchscribe_backend.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.matchscribe.matchscribe_backend.entity.Match;

public interface MatchRepository extends JpaRepository<Match, Long> {
	boolean existsByMatchId(Long matchId);

	Optional<Match> findByMatchId(Long matchId);

	List<Match> findByStatus(String status);

	Optional<Match> findBySlug(String slug);

	@Query("""
			    SELECT m FROM Match m
			    WHERE m.startDatetime >= :fromDate
			""")
	List<Match> findMatchesFromDate(@Param("fromDate") OffsetDateTime fromDate);

}
