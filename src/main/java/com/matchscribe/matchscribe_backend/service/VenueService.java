package com.matchscribe.matchscribe_backend.service;

import java.util.List;

import com.matchscribe.matchscribe_backend.dto.match.VenueDto;
import com.matchscribe.matchscribe_backend.entity.Venue;

public interface VenueService {
	List<Venue> getAllVenues();

	VenueDto getVenueBySlug(String slug);

}
