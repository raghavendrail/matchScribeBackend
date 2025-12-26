package com.matchscribe.matchscribe_backend.controller.publicapi;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matchscribe.matchscribe_backend.dto.match.VenueDto;
import com.matchscribe.matchscribe_backend.entity.ApiResponse;
import com.matchscribe.matchscribe_backend.entity.Venue;
import com.matchscribe.matchscribe_backend.service.VenueService;

@RestController
@RequestMapping("/api/venues")
public class VenueController {
	private final VenueService venueService;

	public VenueController(VenueService venueService) {
		this.venueService = venueService;
	}

	@GetMapping("/all-venues")
	public ResponseEntity<ApiResponse<List<Venue>>> getAllVenues() {
		try {
			List<Venue> venues = venueService.getAllVenues();
			return ResponseEntity.ok(new ApiResponse<>(true, "Venues fetched successfully", venues));
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(500).body(new ApiResponse<>(false, "Failed to fetch venues", null));
		}
	}

	@GetMapping("/{slug}")
	public ResponseEntity<VenueDto> getVenueBySlug(@PathVariable("slug") String slug) {
		VenueDto dto = venueService.getVenueBySlug(slug);
		if (dto == null || dto.venue == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(dto);
	}

}
