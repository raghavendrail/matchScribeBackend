package com.matchscribe.matchscribe_backend.service;

import java.util.List;
import java.util.Optional;

import com.matchscribe.matchscribe_backend.entity.Formats;

public interface FormatService {
	List<Formats> getAllFormats();
	Optional<Formats> getFormatBySlug(String slug);

}
