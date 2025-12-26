package com.matchscribe.matchscribe_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matchscribe.matchscribe_backend.entity.Venue;

public interface VenueRepository extends JpaRepository<Venue, Long> {
	@Override
	boolean existsById(Long id);

	Optional<Venue> findByVenueId(Long venueId);

	Optional<Venue> findBySl(Long venueId);

	Venue findBySlug(String slug);

}
