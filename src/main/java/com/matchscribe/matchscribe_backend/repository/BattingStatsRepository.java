package com.matchscribe.matchscribe_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matchscribe.matchscribe_backend.entity.PlayerCareerBattingStats;

public interface BattingStatsRepository extends JpaRepository<PlayerCareerBattingStats, Long> {

	boolean existsByPlayerSlAndMatchType(Long player, String matchType);

	List<PlayerCareerBattingStats> findByPlayerSl(Long sl);

}
