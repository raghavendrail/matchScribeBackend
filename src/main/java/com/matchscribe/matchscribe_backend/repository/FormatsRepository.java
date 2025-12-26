package com.matchscribe.matchscribe_backend.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matchscribe.matchscribe_backend.entity.Formats;

public interface FormatsRepository extends JpaRepository<Formats, Long> {
	Optional<Formats> findBySlug(String slug);

}
