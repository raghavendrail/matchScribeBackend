package com.matchscribe.matchscribe_backend.repository;

import java.util.Optional;
import com.matchscribe.matchscribe_backend.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
	boolean existsByTeamId(Long teamId);
	Optional<Team> findByTeamId(Long teamId);

}
