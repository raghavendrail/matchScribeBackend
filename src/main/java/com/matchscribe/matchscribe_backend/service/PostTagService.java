package com.matchscribe.matchscribe_backend.service;

import java.util.List;

import com.matchscribe.matchscribe_backend.dto.tag.TagDto;

public interface PostTagService {
	void syncPostTags(Long postId, List<TagDto> tagInputs);

}
