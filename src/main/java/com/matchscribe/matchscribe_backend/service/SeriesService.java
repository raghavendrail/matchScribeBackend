package com.matchscribe.matchscribe_backend.service;

import java.util.List;
import java.util.Optional;
import com.matchscribe.matchscribe_backend.entity.Series;

public interface SeriesService {
	List<Series> getAllSeries();
	Optional<Series> getSeriesBySlug(String slug);

}
