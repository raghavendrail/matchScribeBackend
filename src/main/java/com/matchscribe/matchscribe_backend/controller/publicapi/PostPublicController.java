package com.matchscribe.matchscribe_backend.controller.publicapi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matchscribe.matchscribe_backend.dto.post.PostDto;
import com.matchscribe.matchscribe_backend.service.PostService;

@RestController
@RequestMapping("/api/public")
public class PostPublicController {
	private final PostService postService;

	public PostPublicController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping("/posts/{slug}")
	public ResponseEntity<PostDto> getPostBySlug(@PathVariable("slug") String slug) {
		PostDto dto = postService.getPostBySlug(slug);
		if (dto == null || dto.post == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(dto);
	}

}
