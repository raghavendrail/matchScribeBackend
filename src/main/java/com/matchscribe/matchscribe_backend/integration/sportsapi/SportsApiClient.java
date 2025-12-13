package com.matchscribe.matchscribe_backend.integration.sportsapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matchscribe.matchscribe_backend.config.SportsApiConfig;
import com.matchscribe.matchscribe_backend.integration.sportsapi.dto.SportsMatchDto;

import lombok.RequiredArgsConstructor;

import java.util.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class SportsApiClient {
	private final ObjectMapper objectMapper;
    private final WebClient sportsApiWebClient;
    private final SportsApiConfig props;
    
    
    
    public String getUpcomingMatchesRaw() {
    	//here im getting the error so print the output and handle the error proper 
    	System.out.println("Making request to Sports API: " + props);
		String output = sportsApiWebClient.get()
                .uri("") // baseUrl already has /matches/v1/upcoming
                .header("X-RapidAPI-Key", props.getApiKey())
                .header("X-RapidAPI-Host", props.getHost())
                .retrieve()
                .bodyToMono(String.class)
                .block();	
		System.out.println("output:"+output);
		return output;
    	
        
    }

    // Constructor-based dependency injection
    public SportsApiClient(
            @Qualifier("sportsApiWebClient") WebClient sportsApiWebClient,
            SportsApiConfig props, ObjectMapper objectMapper) {
        this.sportsApiWebClient = sportsApiWebClient;
        this.props = props;
        this.objectMapper = objectMapper;
    }
    
    public String getUpcomingMatches() {
        String json = getUpcomingMatchesRaw();
        List<SportsMatchDto> result = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode typeMatches = root.path("typeMatches");
            if (!typeMatches.isArray()) {
                return "not found";
            }

//            for (JsonNode typeMatchNode : typeMatches) {
//            	//if
//                JsonNode seriesMatches = typeMatchNode.path("seriesMatches");
//                if (!seriesMatches.isArray()) continue;
//
//                for (JsonNode seriesWrapper : seriesMatches) {
//                    JsonNode seriesAdWrapper = seriesWrapper.path("seriesAdWrapper");
//                    if (seriesAdWrapper.isMissingNode()) continue;
//
//                    String seriesName = seriesAdWrapper.path("seriesName").asText(null);
//                    JsonNode matches = seriesAdWrapper.path("matches");
//                    if (!matches.isArray()) continue;
//
//                    for (JsonNode matchWrapper : matches) {
//                        JsonNode matchInfo = matchWrapper.path("matchInfo");
//                        if (matchInfo.isMissingNode()) continue;
//
//                        SportsMatchDto dto = new SportsMatchDto();
//                        dto.setExternalMatchId(matchInfo.path("matchId").asLong());
//                        dto.setSeriesName(seriesName);
//                        dto.setMatchDesc(matchInfo.path("matchDesc").asText(null));
//                        dto.setMatchFormat(matchInfo.path("matchFormat").asText(null));
//                        dto.setStatusText(matchInfo.path("status").asText(null));
//                        dto.setState(matchInfo.path("state").asText(null));
//                        dto.setStartDateEpochMs(matchInfo.path("startDate").asLong());
//                        dto.setEndDateEpochMs(matchInfo.path("endDate").asLong());
//
//                        JsonNode team1 = matchInfo.path("team1");
//                        JsonNode team2 = matchInfo.path("team2");
//                        dto.setTeam1Id(team1.path("teamId").asInt());
//                        dto.setTeam1Name(team1.path("teamName").asText(null));
//                        dto.setTeam1ShortName(team1.path("teamSName").asText(null));
//
//                        dto.setTeam2Id(team2.path("teamId").asInt());
//                        dto.setTeam2Name(team2.path("teamName").asText(null));
//                        dto.setTeam2ShortName(team2.path("teamSName").asText(null));
//
//                        JsonNode venueInfo = matchInfo.path("venueInfo");
//                        dto.setVenueName(venueInfo.path("ground").asText(null));
//                        dto.setCity(venueInfo.path("city").asText(null));
//
//                        result.add(dto);
//                    }
//                }
//            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse sports API response", e);
        }

        return json;
    }

//    public String getUpcomingMatchesRaw() {
//        return sportsApiWebClient.get()
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//    }
}