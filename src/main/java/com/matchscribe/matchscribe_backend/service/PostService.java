package com.matchscribe.matchscribe_backend.service;

import com.matchscribe.matchscribe_backend.dto.post.PostDto;
import com.matchscribe.matchscribe_backend.util.ImportResult;

public interface PostService {
	PostDto getPostBySlug(String slug);

	ImportResult generatePostsForUpcomingMatches();

}
