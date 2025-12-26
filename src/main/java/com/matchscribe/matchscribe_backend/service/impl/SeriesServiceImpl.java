package com.matchscribe.matchscribe_backend.service.impl;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.matchscribe.matchscribe_backend.entity.Series;
import com.matchscribe.matchscribe_backend.repository.SeriesRepository;
import com.matchscribe.matchscribe_backend.service.SeriesService;
@Service
public class SeriesServiceImpl implements SeriesService {
	private final SeriesRepository seriesRepository;
	public SeriesServiceImpl(SeriesRepository seriesRepository) {
		this.seriesRepository = seriesRepository;
	}
	@Override
	public List<Series> getAllSeries() {
		return seriesRepository.findAll();
	}
	@Override
	public Optional<Series> getSeriesBySlug(String slug) {
		return seriesRepository.findBySlug(slug);
	}

}
