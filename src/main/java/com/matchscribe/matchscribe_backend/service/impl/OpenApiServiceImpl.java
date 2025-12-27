package com.matchscribe.matchscribe_backend.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.matchscribe.matchscribe_backend.config.OpenAiConfig;
import com.matchscribe.matchscribe_backend.service.OpenApiService;

@Service
public class OpenApiServiceImpl implements OpenApiService {

	private final WebClient openAiWebClient;
	private final OpenAiConfig config;

	public OpenApiServiceImpl(@Qualifier("openAiWebClient") WebClient openAiWebClient, OpenAiConfig config) {
		this.openAiWebClient = openAiWebClient;
		this.config = config;
	}

	// -------------------------------
	// Generic AI call
	// -------------------------------
	@Override
	public String generateText(String input) {
		return callOpenAi(input);
	}

	// -------------------------------
	// Post Section Generators
	// -------------------------------
	@Override
	public String generateSummary(String matchPayload) {

		String prompt = """
				You are a professional sports content editor.

				TASK:
				Write a concise summary for the upcoming cricket match using the data below.

				GUIDELINES:
				1. Limit to 2–3 sentences
				2. Clearly mention teams, match context, and format
				3. Keep tone neutral and informative
				4. Do NOT include predictions or betting language

				STYLE:
				- Clear
				- Reader-friendly
				- SEO suitable
				- Public safe

				MATCH DATA:
				""" + matchPayload;

		return callOpenAi(prompt);
	}

	@Override
	public String generateTitle(String matchPayload) {

		String prompt = """
				You are a professional sports headline writer.

				TASK:
				Generate a clear and engaging match title based on the data below.

				GUIDELINES:
				1. Include both team names
				2. Mention match format or series if relevant
				3. Keep title under 70 characters
				4. Avoid clickbait or predictions

				STYLE:
				- Clean
				- Informative
				- SEO-friendly

				MATCH DATA:
				""" + matchPayload;

		return callOpenAi(prompt);
	}

	@Override
	public String[] generateTagSlug(String matchPayload) {
		return callOpenAiForTags(matchPayload);
	}

	@Override
	public String generateSeoTitle(String matchPayload) {

		String prompt = """
				You are an SEO content strategist.

				TASK:
				Generate an SEO-optimized meta title for a cricket match page.

				RULES:
				1. Maximum 60 characters
				2. Include both team names
				3. Mention match format or series if relevant
				4. Avoid repetition or keyword stuffing
				5. No emojis or special characters

				STYLE:
				- Clear
				- Professional
				- Search engine friendly

				MATCH DATA:
				""" + matchPayload;

		return callOpenAi(prompt);
	}

	@Override
	public String generateSeoDescription(String matchPayload) {

		String prompt = """
				You are an SEO content strategist.

				TASK:
				Write an SEO-friendly meta description for a cricket match page.

				RULES:
				1. Length between 140–160 characters
				2. Mention teams and match context
				3. Encourage clicks naturally
				4. Avoid predictions or guarantees

				STYLE:
				- Informative
				- Neutral
				- Search optimized

				MATCH DATA:
				""" + matchPayload;

		return callOpenAi(prompt);
	}

	@Override
	public String generateOverview(String matchPayload) {

		String prompt = """
				You are a professional cricket content writer.

				TASK:
				Write a detailed and engaging overview for the upcoming cricket match using the data below.

				GUIDELINES:
				1. Introduce the match context clearly:
				   - Teams involved
				   - Tournament or series
				   - Match format (T20 / ODI / Test)
				2. Highlight why the match is important
				3. Mention recent form or momentum briefly (without deep analysis)
				4. Keep tone neutral and informative
				5. Avoid predictions or guaranteed outcomes

				CONTENT STRUCTURE:
				- 2–3 short paragraphs
				- Smooth and engaging flow
				- Suitable for both casual readers and cricket fans

				STYLE:
				- Clear and engaging
				- Professional and neutral
				- SEO-friendly
				- Suitable for public sports platforms

				MATCH DATA:
				""" + matchPayload;

		return callOpenAi(prompt);
	}

	@Override
	public String generateTeamForm(String matchPayload) {

		String prompt = """
				You are a professional cricket analyst.

				TASK:
				Analyze the recent form of BOTH teams based on the match data below.

				GUIDELINES:
				1. Consider:
				   - Results from recent matches
				   - Batting and bowling performance trends
				   - Consistency and momentum
				2. Mention strengths and areas of concern for each team
				3. Keep analysis balanced and factual
				4. Avoid predicting the match winner or final result

				CONTENT STRUCTURE:
				- Separate short section for each team
				- 2–3 analytical points per team

				STYLE:
				- Informative and neutral
				- Easy to read
				- SEO-friendly
				- Suitable for public cricket content

				MATCH DATA:
				""" + matchPayload;

		return callOpenAi(prompt);
	}

	@Override
	public String generatePitchReport(String matchPayload) {

		String prompt = """
				You are a professional cricket pitch analyst.

				TASK:
				Provide a detailed pitch report for the upcoming match using the data below.

				GUIDELINES:
				1. Analyze pitch characteristics such as:
				   - Batting-friendly or bowling-friendly nature
				   - Pace vs spin assistance
				   - Bounce and carry
				2. Consider:
				   - Venue history
				   - Match format (T20 / ODI / Test)
				   - Typical first-innings and second-innings behavior
				3. Mention how the pitch may evolve during the match
				4. Avoid predicting exact scores or guaranteed outcomes

				CONTENT STRUCTURE:
				- 1 short introductory paragraph
				- 2–3 analytical points

				STYLE:
				- Neutral and informative
				- Clear and easy to read
				- SEO-friendly language
				- Suitable for public cricket content

				MATCH DATA:
				""" + matchPayload;

		return callOpenAi(prompt);
	}

	@Override
	public String generateWeatherReport(String matchPayload) {

		String prompt = """
				You are a professional sports weather analyst.

				TASK:
				Provide a concise weather report for the match using the data below and explain its possible impact.

				GUIDELINES:
				1. Mention expected weather conditions such as:
				   - Temperature
				   - Cloud cover
				   - Rain probability
				   - Humidity and wind (if relevant)
				2. Explain possible match impact:
				   - Swing or seam assistance
				   - Dew factor
				   - Rain interruptions or delays
				3. Keep the tone informational and neutral
				4. Avoid definitive predictions or guarantees

				CONTENT STRUCTURE:
				- 1 short paragraph describing weather
				- 1 short paragraph describing possible impact

				STYLE:
				- Clear and concise
				- Easy to understand
				- SEO-friendly
				- Suitable for public sports platforms

				MATCH DATA:
				""" + matchPayload;

		return callOpenAi(prompt);
	}

	@Override
	public String generateKeyPlayers(String matchPayload) {

		String prompt = """
				You are a professional cricket analyst.

				TASK:
				Identify and analyze the key players to watch in this match based on the data below.

				GUIDELINES:
				1. Select 4–6 key players in total
				2. Include players from BOTH teams
				3. Consider:
				   - Recent performance and consistency
				   - Player role and match impact
				   - Venue and pitch suitability
				   - Match format and series context
				4. Avoid injured or non-playing players
				5. Do NOT use betting or guaranteed outcome language

				CONTENT STRUCTURE:
				- Player Name (Team): 1–2 lines explaining why they are important
				- Use short paragraphs or bullet-style lines (plain text)

				STYLE:
				- Informative
				- Neutral
				- Easy to read
				- SEO-friendly

				MATCH DATA:
				""" + matchPayload;

		return callOpenAi(prompt);
	}

	@Override
	public String generateHeadToHead(String matchPayload) {

		String prompt = """
				You are a professional cricket analyst.

				TASK:
				Provide a clear head-to-head analysis between the two teams based on the match data below.

				GUIDELINES:
				1. Include:
				   - Overall head-to-head record (if available)
				   - Recent meetings trend
				   - Venue-specific performance (if relevant)
				2. Highlight patterns such as:
				   - Dominant team
				   - Close contests
				   - Format-specific trends
				3. Avoid exact score predictions or guaranteed outcomes
				4. Keep analysis factual and balanced

				CONTENT STRUCTURE:
				- Short introductory paragraph
				- 2–3 concise analytical points

				STYLE:
				- Neutral
				- Informative
				- Easy to understand
				- Suitable for public sports content

				MATCH DATA:
				""" + matchPayload;

		return callOpenAi(prompt);
	}

	@Override
	public Integer[] generateProbableXi(String matchPayload, String teamType) {

		String prompt = """
				You are a professional cricket team analyst.

				TASK:
				Select the MOST LIKELY playing XI for the %s team based on the match data below.

				STRICT RULES (must follow all):
				1. Select exactly 11 players
				2. Use ONLY players from the %s team squad
				3. Team composition must include:
				   - At least 1 Wicketkeeper
				   - At least 3 Batsmen
				   - At least 3 Bowlers
				   - At least 1 All-rounder
				4. Do NOT select all players of a single role
				5. Consider:
				   - Match format (T20 / ODI / Test)
				   - Venue and pitch conditions
				   - Home/Away advantage
				   - Recent player form
				   - Team balance and role clarity
				6. Exclude injured, suspended, or inactive players

				OUTPUT FORMAT (VERY IMPORTANT):
				Return ONLY valid JSON.
				No explanation text.
				No markdown.

				JSON SCHEMA:
				{
				  "players": [playerId1, playerId2, ..., playerId11]
				}

				MATCH DATA:
				%s
				""".formatted(teamType, teamType, matchPayload);

		return callOpenAiForXi(prompt);
	}

	@Override
	public Integer[] generatePrediction(String matchPayload) {

		String prompt = """
				You are a professional fantasy cricket analyst.

				TASK:
				Select the BEST possible fantasy playing XI based on the match data below.

				STRICT RULES (must follow all):
				1. Total players = 11
				2. Maximum 7 players from a single team
				3. Minimum composition:
				   - 1 Wicketkeeper
				   - 3 Batsmen
				   - 3 Bowlers
				   - 1 All-rounder
				4. Do NOT select all players from one team
				5. Consider ONLY probable playing XI players
				6. Prioritize:
				   - Venue conditions (spin/seam/high scoring)
				   - Recent form (last 5 matches)
				   - Match type (T20/ODI/Test)
				   - Series importance
				   - Player role balance
				7. Avoid injured or inactive players

				OUTPUT FORMAT (VERY IMPORTANT):
				- Return ONLY valid JSON
				- No explanation text
				- No markdown
				- No comments

				JSON SCHEMA:
				{
				  "players": [playerId1, playerId2, ..., playerId11]
				}

				MATCH DATA:
				""" + matchPayload;

		return callOpenAiForPrediction(prompt);
	}

	@Override
	public String generateFantasyTips(String matchPayload) {

		String prompt = """
				You are an expert fantasy cricket analyst.

				TASK:
				Provide well-balanced fantasy cricket tips for the upcoming match using the data below.

				GUIDELINES:
				1. Suggest players only from probable playing XI
				2. Consider:
				   - Venue conditions (spin/seam/high scoring ground)
				   - Match format (T20 / ODI / Test)
				   - Recent player form (last few matches)
				   - Team balance and role clarity
				3. Do NOT guarantee outcomes
				4. Avoid betting or gambling language

				CONTENT STRUCTURE (follow strictly):
				- Top Fantasy Picks (3–5 players)
				- Safe Captain Choices (1–2 players)
				- Differential Vice-Captain Options (1–2 players)
				- Risky Picks (optional, max 2)
				- One short disclaimer sentence (informational purpose only)

				STYLE:
				- Clear
				- Concise
				- Neutral
				- Professional
				- SEO friendly

				MATCH DATA:
				""" + matchPayload;

		return callOpenAi(prompt);
	}

	@Override
	public String generateFinalVerdict(String matchPayload) {

		String prompt = """
				You are a professional cricket analyst.

				TASK:
				Write a concise and neutral final verdict for the upcoming match based on the data below.

				GUIDELINES:
				1. Summarize overall match expectations
				2. Highlight key factors such as:
				   - Team strengths
				   - Venue influence
				   - Recent form
				   - Match format impact
				3. Do NOT predict guaranteed winners
				4. Keep tone balanced and informative

				STYLE:
				- Maximum 3 short paragraphs
				- Clear and engaging
				- Neutral and responsible language
				- Suitable for public sports content

				MATCH DATA:
				""" + matchPayload;

		return callOpenAi(prompt);
	}

	// -------------------------------
	// PRIVATE OpenAI Helper
	// -------------------------------
	private String callOpenAi(String prompt) {

		Map<String, Object> request = Map.of("model", config.getModel(), "input",
				List.of(Map.of("role", "user", "content", List.of(Map.of("type", "text", "text", prompt)))));

		return openAiWebClient.post().uri("/responses").bodyValue(request).retrieve().bodyToMono(Map.class).map(res -> {
			List<Map<String, Object>> output = (List<Map<String, Object>>) res.get("output");
			List<Map<String, Object>> content = (List<Map<String, Object>>) output.get(0).get("content");
			return content.get(0).get("text").toString();
		}).block();
	}

	private Integer[] callOpenAiForPrediction(String prompt) {

		Map<String, Object> request = Map.of("model", config.getModel(), "response_format",
				Map.of("type", "json_object"), "input",
				List.of(Map.of("role", "user", "content", List.of(Map.of("type", "text", "text", prompt)))));

		return openAiWebClient.post().uri("/responses").bodyValue(request).retrieve().bodyToMono(Map.class).map(res -> {
			List<Map<String, Object>> output = (List<Map<String, Object>>) res.get("output");
			List<Map<String, Object>> content = (List<Map<String, Object>>) output.get(0).get("content");

			Map<String, Object> json = (Map<String, Object>) content.get(0).get("json");

			List<Integer> players = (List<Integer>) json.get("players");

			// SAFETY CHECK
			if (players == null || players.size() != 11) {
				throw new RuntimeException("AI returned invalid team size");
			}

			return players.toArray(new Integer[0]);
		}).block();
	}

	private Integer[] callOpenAiForXi(String prompt) {

		Map<String, Object> request = Map.of("model", config.getModel(), "response_format",
				Map.of("type", "json_object"), "input",
				List.of(Map.of("role", "user", "content", List.of(Map.of("type", "text", "text", prompt)))));

		return openAiWebClient.post().uri("/responses").bodyValue(request).retrieve().bodyToMono(Map.class).map(res -> {
			List<Map<String, Object>> output = (List<Map<String, Object>>) res.get("output");
			List<Map<String, Object>> content = (List<Map<String, Object>>) output.get(0).get("content");

			Map<String, Object> json = (Map<String, Object>) content.get(0).get("json");

			List<Integer> players = (List<Integer>) json.get("players");

			if (players == null || players.size() != 11) {
				throw new RuntimeException("Invalid playing XI generated by AI");
			}

			return players.toArray(new Integer[0]);
		}).block();
	}

	private String[] callOpenAiForTags(String matchPayload) {

		String prompt = """
				You are an SEO specialist for a cricket content platform.

				TASK:
				Generate SEO-friendly tag slugs for the match based on the data below.

				RULES:
				1. Generate 6–10 tag slugs
				2. Use lowercase letters only
				3. Use hyphen (-) instead of spaces
				4. No special characters
				5. Focus on:
				   - Team names
				   - Tournament or series
				   - Match format
				   - Venue or location
				6. Avoid duplicate or generic tags

				OUTPUT FORMAT (STRICT):
				Return ONLY valid JSON.
				No text outside JSON.

				JSON SCHEMA:
				{
				  "tags": ["tag-one", "tag-two", "tag-three"]
				}

				MATCH DATA:
				""" + matchPayload;

		Map<String, Object> request = Map.of("model", config.getModel(), "response_format",
				Map.of("type", "json_object"), "input",
				List.of(Map.of("role", "user", "content", List.of(Map.of("type", "text", "text", prompt)))));

		return openAiWebClient.post().uri("/responses").bodyValue(request).retrieve().bodyToMono(Map.class).map(res -> {
			List<Map<String, Object>> output = (List<Map<String, Object>>) res.get("output");
			List<Map<String, Object>> content = (List<Map<String, Object>>) output.get(0).get("content");

			Map<String, Object> json = (Map<String, Object>) content.get(0).get("json");

			List<String> tags = (List<String>) json.get("tags");

			if (tags == null || tags.isEmpty()) {
				throw new RuntimeException("AI returned empty tag list");
			}

			return tags.toArray(new String[0]);
		}).block();
	}

	public List<String> generateTags(String content) {

		if (content == null || content.isBlank()) {
			return List.of();
		}

		String prompt = """
				You generate SEO-friendly NAVIGATION TAGS for a cricket match prediction website.

				Tags are NOT for decoration.
				They must help users navigate to specific pages.

				WEBSITE FOCUS:
				- match previews
				- fantasy team suggestions
				- pitch & weather insights
				- safe picks vs risky picks
				- captain / vice-captain choices

				TAG COUNT:
				- return 6 to 8 tags only

				OUTPUT FORMAT:
				- lowercase
				- slug style (use hyphens)
				- short (1–3 words max)
				- comma separated only
				- no hashtags
				- no numbers
				- no explanations
				- no quotes

				DO NOT GENERATE:
				- player names
				- match records / history
				- statistics
				- betting or gambling terms
				- generic words like: cricket, match, sports, article

				ALLOWED TAG CATEGORIES ONLY:

				1) venue name
				   (example: wankhede-stadium, eden-gardens)

				2) home team name
				   (example: india, australia, chennai-super-kings)

				3) away team name
				   same format as above

				4) series or tournament name
				   (example: world-cup, asia-cup, ipl)

				5) fantasy / dream11 strategy or content type
				   (example: fantasy-tips, captain-picks, safe-picks, pitch-report, weather-report)

				Example output format:
				wankhede-stadium, india, australia, world-cup, fantasy-tips, captain-picks, pitch-report

				Now generate tags for this content:

				""" + content;

		String response = callOpenAi(prompt);

		return parseTags(response);
	}

	/**
	 * Cleans and normalizes OpenAI output
	 */
	private List<String> parseTags(String response) {

		List<String> tags = new ArrayList<>();

		if (response == null || response.isBlank()) {
			return tags;
		}

		String cleaned = response.replaceAll("[\\n\\r]", ",").replaceAll("#", "").trim();

		for (String part : cleaned.split(",")) {
			String tag = part.trim();

			if (tag.length() < 2) {
				continue;
			}
			if (tag.length() > 40) {
				continue;
			}

			tags.add(tag);
		}

		return tags;
	}

	@Override
	public String generateVenueDescription(JsonNode venueInfo, JsonNode statsInfo) {

		String prompt = """
				You are a professional cricket content writer.

				TASK:
				Write an engaging and informative venue description using the data below.

				GUIDELINES:
				1. Include key details such as:
				   - Location (city, country)
				   - Capacity
				   - Establishment year
				   - Pitch characteristics
				   - Notable matches or events hosted
				2. Integrate relevant statistics naturally
				3. Keep tone neutral and informative
				4. Avoid promotional language

				STYLE:
				- Clear and engaging
				- Professional and neutral
				- SEO-friendly
				- Suitable for public sports platforms

				VENUE DATA:
				""" + venueInfo + "\n\nVENUE STATS:\n" + statsInfo;

		return callOpenAi(prompt);
	}

	@Override
	public String generateFormatDescription(String format) {

		String prompt = """
				You are a professional cricket content writer.

				TASK:
				Write a clear and concise description of the cricket format using the data below.

				GUIDELINES:
				1. Explain key aspects such as:
				   - Match duration
				   - Number of overs
				   - Fielding restrictions
				   - Unique rules or features
				2. Keep tone neutral and informative
				3. Avoid jargon; make it accessible for casual readers

				STYLE:
				- Clear and concise
				- Professional and neutral
				- SEO-friendly
				- Suitable for public sports platforms

				FORMAT DATA:
				""" + format;

		return callOpenAi(prompt);
	}

	@Override
	public String generateSeriesDescription(String slug) {

		String prompt = """
								You are a professional cricket journalist and content strategist.

				Your task is to generate a high-quality, engaging article for a cricket series
				that keeps readers interested till the end.

				RULES (MANDATORY):
				- Return ONLY valid JSON
				- No HTML
				- No markdown
				- No explanations
				- No extra keys
				- Write in a natural, engaging, human tone
				- Avoid repetition
				- Content must feel premium and informative

				JSON FORMAT:
				{
				  "hookLine": "string",
				  "overview": "string",
				  "playingStyle": "string",
				  "whyThisSeriesMatters": "string",
				  "keyHighlights": ["string", "string", "string"],
				  "fanExperience": "string",
				  "talentImpact": "string",
				  "globalRelevance": "string",
				  "conclusion": "string",
				  "readingTimeMinutes": number
				}

				SERIES DETAILS:
				Series Name: {{seriesName}}
				Country / League: {{leagueName}}
				Format: {{format}}

								""" + slug;

		return callOpenAi(prompt);
	}

}
