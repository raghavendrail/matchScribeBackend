package com.matchscribe.matchscribe_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matchscribe.matchscribe_backend.entity.VenueStats;

public interface VenueStatsRepository extends JpaRepository<VenueStats, Long> {
	List<VenueStats> findVenueStatsByVenueSl(Long venueSl);

}
