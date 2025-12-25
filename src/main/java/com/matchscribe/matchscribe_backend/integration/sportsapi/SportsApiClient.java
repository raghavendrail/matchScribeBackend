package com.matchscribe.matchscribe_backend.integration.sportsapi;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matchscribe.matchscribe_backend.config.SportsApiConfig;

@Component
public class SportsApiClient {
	private final ObjectMapper objectMapper;
	private final WebClient sportsApiWebClient;
	private final SportsApiConfig props;

	// Constructor-based dependency injection
	public SportsApiClient(@Qualifier("sportsApiWebClient") WebClient sportsApiWebClient, SportsApiConfig props,
			ObjectMapper objectMapper) {
		this.sportsApiWebClient = sportsApiWebClient;
		this.props = props;
		this.objectMapper = objectMapper;
	}

	private String callSportsApi(String url) {
		try {
			System.out.println("Calling Sports API URL: " + url);

			return sportsApiWebClient.get().uri(url).header("X-RapidAPI-Key", props.getApiKey())
					.header("X-RapidAPI-Host", props.getHost()).retrieve().bodyToMono(String.class).block();

		} catch (Exception e) {
			throw new RuntimeException("Sports API request failed", e);
		}
	}

	public String getUpcomingMatches() {
		return callSportsApi(props.getUpcomingMatchesUrl());
	}

	public String getTeamPlayersRaw(int teamId) {
		return callSportsApi(props.getTeamPlayersUrl(teamId));
	}

	public String getTeamInfoRow(int matchId, int teamId) {
		return callSportsApi(props.getTeamUrl(matchId, teamId));
	}

	public String getCompletedMatches(int matchId) {
		return callSportsApi(props.getCompletedMatchesUrl(matchId));
	}

	public String getPlayerBattingStats(int playerId) {
		return callSportsApi(props.getBattingStatsUrl(playerId));
	}

	public String getPlayerBowlingStats(int playerId) {
		return callSportsApi(props.getBowlingStatsUrl(playerId));
	}
	
	public String getVenueInfo(int venueId) {
		return callSportsApi(props.getVenueInfo(venueId));
	}
	public String getVenueStats(int venueId) {
		return callSportsApi(props.getVenueStats(venueId));
	}

}