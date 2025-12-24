package com.matchscribe.matchscribe_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matchscribe.matchscribe_backend.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

	Optional<Tag> findBySlug(String slug);

	Optional<Tag> findByNameIgnoreCase(String name);
}