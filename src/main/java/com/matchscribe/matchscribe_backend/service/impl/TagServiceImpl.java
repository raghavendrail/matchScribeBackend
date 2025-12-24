package com.matchscribe.matchscribe_backend.service.impl;

import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matchscribe.matchscribe_backend.entity.Tag;
import com.matchscribe.matchscribe_backend.repository.TagRepository;
import com.matchscribe.matchscribe_backend.service.TagService;

@Service
@Transactional
public class TagServiceImpl implements TagService {

	private final TagRepository tagRepository;

	public TagServiceImpl(TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}

	@Override
	public Tag getOrCreate(String name, String tagType) {

		String cleanName = name.trim();
		String slug = toSlug(cleanName);

		Tag tag = tagRepository.findBySlug(slug).orElse(null);

		if (tag == null) {
			tag = new Tag();
			tag.setName(cleanName);
			tag.setSlug(slug);
			tag.setTagType(tagType);
			tag.setUsageCount(0);
			tag.setIsActive(true);
			tag = tagRepository.save(tag);
		}

		return tag;
	}

	@Override
	public void incrementUsage(Tag tag) {
		tag.setUsageCount(tag.getUsageCount() + 1);
		tagRepository.save(tag);
	}

	@Override
	public void decrementUsage(Tag tag) {
		if (tag.getUsageCount() > 0) {
			tag.setUsageCount(tag.getUsageCount() - 1);
			tagRepository.save(tag);
		}
	}

	private String toSlug(String input) {
		return input.toLowerCase(Locale.ENGLISH).replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
	}
}
