package com.matchscribe.matchscribe_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matchscribe.matchscribe_backend.entity.PlayerCareerBowlingStats;

public interface BowlingStatsRepository extends JpaRepository<PlayerCareerBowlingStats, Long> {

	boolean existsByPlayerSlAndMatchType(Long player, String matchType);

	List<PlayerCareerBowlingStats> findByPlayerSl(Long sl);

}
