package com.matchscribe.matchscribe_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.matchscribe.matchscribe_backend.entity.*;

public interface MatchRepository  extends JpaRepository<Match, Long>{
	boolean existsByMatchId(Long matchId);
	Optional<Match> findByMatchId(Long matchId);
}
