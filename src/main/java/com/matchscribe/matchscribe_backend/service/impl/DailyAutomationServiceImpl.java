package com.matchscribe.matchscribe_backend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matchscribe.matchscribe_backend.service.DailyAutomationService;
import com.matchscribe.matchscribe_backend.service.MatchService;
import com.matchscribe.matchscribe_backend.service.PostService;

@Service
public class DailyAutomationServiceImpl implements DailyAutomationService {

	private final MatchService matchService;
	private final PostService postService;

	public DailyAutomationServiceImpl(MatchService matchService, PostService postService) {
		this.matchService = matchService;
		this.postService = postService;
	}

	@Override
	@Transactional
	public void runDailyAutomation() {

		// 1. Import / update matches from RapidAPI
		matchService.importUpcomingMatchesFromSportsApi();

		// 2. Update match statuses (scheduled → completed)
		matchService.updateRecentMatchesFromDb();

		// 3. Generate placeholder posts for matches without content
		postService.generatePostsForUpcomingMatches();

		// 4. Archive old matches
		// matchService.archiveOldMatches();
	}
}
