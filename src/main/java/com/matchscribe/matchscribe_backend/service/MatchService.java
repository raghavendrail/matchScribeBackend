package com.matchscribe.matchscribe_backend.service;

import java.util.List;

import com.matchscribe.matchscribe_backend.dto.match.MatchDto;
import com.matchscribe.matchscribe_backend.util.ImportResult;

public interface MatchService {
	ImportResult importUpcomingMatchesFromSportsApi();

	MatchDto getMatchById(Long matchId);

	Iterable<MatchDto> getAllMatches();

	List<MatchDto> getUpcomingMatches();

	MatchDto getMatchBySlug(String slug);

}
