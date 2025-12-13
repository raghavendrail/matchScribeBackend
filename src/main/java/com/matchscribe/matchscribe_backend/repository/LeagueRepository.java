package com.matchscribe.matchscribe_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matchscribe.matchscribe_backend.entity.*;

public interface LeagueRepository extends JpaRepository<League, Long> {
	Optional<League> findByName(String name);

}
