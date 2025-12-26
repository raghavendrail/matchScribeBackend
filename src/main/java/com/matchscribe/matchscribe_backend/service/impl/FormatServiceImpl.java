package com.matchscribe.matchscribe_backend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.matchscribe.matchscribe_backend.entity.Formats;
import com.matchscribe.matchscribe_backend.repository.FormatsRepository;
import com.matchscribe.matchscribe_backend.service.FormatService;
@Service
public class FormatServiceImpl implements FormatService {
	private final FormatsRepository formatRepository;
	public FormatServiceImpl(FormatsRepository formatRepository) {
		this.formatRepository = formatRepository;
	}
	@Override
	public List<Formats> getAllFormats() {
		return formatRepository.findAll();
	}
	@Override
	public Optional<Formats> getFormatBySlug(String slug) {
		return formatRepository.findBySlug(slug);
	}

}
