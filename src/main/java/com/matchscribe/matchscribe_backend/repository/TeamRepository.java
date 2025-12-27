package com.matchscribe.matchscribe_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matchscribe.matchscribe_backend.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
	boolean existsByTeamId(Long teamId);

	Optional<Team> findByTeamId(Long teamId);

	Optional<Team> findBySl(Long teamId);

	String findNameBySl(Long teamId);

	Team findBySlug(String slug);

}
