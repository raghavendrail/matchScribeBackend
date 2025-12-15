package com.matchscribe.matchscribe_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matchscribe.matchscribe_backend.entity.Series;

public interface SeriesRepository extends JpaRepository<Series, Long>{
//boolean exists = SeriesRepository.existsBySeriesId(seriesWrapper.path("seriesAdWrapper").path("seriesId").asInt());
	boolean existsBySeriesId(Long seriesId);
	 Optional<Series> findBySeriesId(Long seriesId);
	 Optional<Series> findBySl(Long seriesId);
}
