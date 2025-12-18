package com.matchscribe.matchscribe_backend.service;

import com.matchscribe.matchscribe_backend.dto.post.PostDto;

public interface PostService {
	PostDto getPostBySlug(String slug);

}
