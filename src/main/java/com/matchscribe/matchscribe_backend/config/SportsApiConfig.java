package com.matchscribe.matchscribe_backend.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sportsapi")
public class SportsApiConfig {

	private Map<String, String> baseUrl;
	private String host;
	private String apiKey;
	private int timeoutMs;

	// Getters
	public Map<String, String> getBaseUrl() {
		return baseUrl;
	}

	public String getHost() {
		return host;
	}

	public String getApiKey() {
		return apiKey;
	}

	public int getTimeoutMs() {
		return timeoutMs;
	}

	// Setters
	public void setBaseUrl(Map<String, String> baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setTimeoutMs(int timeoutMs) {
		this.timeoutMs = timeoutMs;
	}

	@Override
	public String toString() {
		return "SportsApiConfig{" + "baseUrl='" + baseUrl + '\'' + ", host='" + host + '\'' + ", apiKey='[PROTECTED]'"
				+ ", timeoutMs=" + timeoutMs + '}';
	}

	public String getUpcomingMatchesUrl() {
		return baseUrl.get("matches-upcoming");
	}

	public String getTeamPlayersUrl(int teamId) {
		return baseUrl.get("team-players").replace("{teamId}", String.valueOf(teamId));
	}

	public String getTeamUrl(int matchId, int teamId) {
		return baseUrl.get("team-info").replace("{matchId}", String.valueOf(matchId)).replace("{teamId}",
				String.valueOf(teamId));
	}

	public String getCompletedMatchesUrl(int matchId) {
		return baseUrl.get("matches-completed").replace("{matchId}", String.valueOf(matchId));
	}

	public String getBattingStatsUrl(int playerId) {
		return baseUrl.get("player-batting-stats").replace("{playerId}", String.valueOf(playerId));
	}

	public String getBowlingStatsUrl(int playerId) {
		return baseUrl.get("player-bowling-stats").replace("{playerId}", String.valueOf(playerId));
	}
	public String getVenueInfo(int venueId) {
		return baseUrl.get("venue-info").replace("{venueId}", String.valueOf(venueId));
	}
	public String getVenueStats(int venueId) {
		return baseUrl.get("venue-stats").replace("{venueId}", String.valueOf(venueId));
	}

}