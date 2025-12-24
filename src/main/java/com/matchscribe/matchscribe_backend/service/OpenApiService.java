package com.matchscribe.matchscribe_backend.service;

public interface OpenApiService {
	String generateTitle(String prompt);

	String generateSummary(String matchPayload);

	String[] generateTagSlug(String matchPayload);

	String generateSeoTitle(String matchPayload);

	String generateSeoDescription(String matchPayload);

	String generateText(String prompt);

	String generateOverview(String matchPayload);

	String generateTeamForm(String matchPayload);

	String generatePitchReport(String matchPayload);

	String generateWeatherReport(String matchPayload);

	String generateKeyPlayers(String matchPayload);

	String generateHeadToHead(String matchPayload);

	Integer[] generateProbableXi(String matchPayload, String teamType);

	Integer[] generatePrediction(String matchPayload);

	String generateFantasyTips(String matchPayload);

	String generateFinalVerdict(String matchPayload);

}
