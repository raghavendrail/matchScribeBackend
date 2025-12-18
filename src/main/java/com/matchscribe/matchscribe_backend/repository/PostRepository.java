package com.matchscribe.matchscribe_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matchscribe.matchscribe_backend.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	Optional<Post> findByMatchId(Long matchId);

	Optional<Post> findBySlug(String slug);

}
