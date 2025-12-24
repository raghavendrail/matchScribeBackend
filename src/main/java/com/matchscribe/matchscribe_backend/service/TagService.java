package com.matchscribe.matchscribe_backend.service;

import com.matchscribe.matchscribe_backend.entity.Tag;

public interface TagService {
	Tag getOrCreate(String name, String tagType);

	void incrementUsage(Tag tag);

	void decrementUsage(Tag tag);
}
