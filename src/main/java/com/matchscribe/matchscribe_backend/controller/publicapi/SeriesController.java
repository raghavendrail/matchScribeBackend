package com.matchscribe.matchscribe_backend.controller.publicapi;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matchscribe.matchscribe_backend.entity.ApiResponse;
import com.matchscribe.matchscribe_backend.entity.Series;
import com.matchscribe.matchscribe_backend.service.SeriesService;

@RestController
@RequestMapping("/api/series")
public class SeriesController {
	private final SeriesService seriesService;
	public SeriesController(SeriesService seriesService) {
		this.seriesService = seriesService;
	}
	@GetMapping("/all-series")
	public ResponseEntity<ApiResponse<List<Series>>> getAllSeries() {
		try {
			List<Series> series = seriesService.getAllSeries();
			return ResponseEntity.ok(new ApiResponse<>(true, "series fetched successfully", series));
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(500).body(new ApiResponse<>(false, "Failed to fetch series", null));
		}
	}

	@GetMapping("/{slug}")
	public ResponseEntity<Series> getSeriesBySlug(@PathVariable("slug") String slug) {
		Optional<Series> series = seriesService.getSeriesBySlug(slug);
		if (series == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(series.get());
	}


}
