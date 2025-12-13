package com.matchscribe.matchscribe_backend.service;

import com.matchscribe.matchscribe_backend.dto.match.MatchDto;

public interface MatchService {
	void importUpcomingMatchesFromSportsApi();
	MatchDto getMatchById(Long matchId);
	Iterable<MatchDto> getAllMatches();

}
