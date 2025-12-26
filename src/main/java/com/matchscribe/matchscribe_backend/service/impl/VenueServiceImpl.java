package com.matchscribe.matchscribe_backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matchscribe.matchscribe_backend.dto.match.VenueDto;
import com.matchscribe.matchscribe_backend.entity.Venue;
import com.matchscribe.matchscribe_backend.entity.VenueStats;
import com.matchscribe.matchscribe_backend.repository.VenueRepository;
import com.matchscribe.matchscribe_backend.repository.VenueStatsRepository;
import com.matchscribe.matchscribe_backend.service.VenueService;

@Service
public class VenueServiceImpl implements VenueService {
	private final VenueRepository venueRepository;
	private final VenueStatsRepository venueStatsRepository;

	public VenueServiceImpl(VenueRepository venueRepository, VenueStatsRepository venueStatsRepository) {
		this.venueRepository = venueRepository;
		this.venueStatsRepository = venueStatsRepository;
	}

	@Override
	public List<Venue> getAllVenues() {
		return venueRepository.findAll();
	}

	@Override
	public VenueDto getVenueBySlug(String slug) {
		Venue venue = venueRepository.findBySlug(slug);
		if (venue == null) {
			return null;
		}
		List<VenueStats> stats = venueStatsRepository.findVenueStatsByVenueSl(venue.getSl());
		VenueDto dto = new VenueDto();
		dto.venue = venue;
		dto.venueStats = stats;
		return dto;
	}

}
