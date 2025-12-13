package com.matchscribe.matchscribe_backend.service.impl;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matchscribe.matchscribe_backend.dto.match.MatchDto;
import com.matchscribe.matchscribe_backend.entity.*;
import com.matchscribe.matchscribe_backend.entity.enums.MatchResultType;
import com.matchscribe.matchscribe_backend.entity.enums.MatchStatus;
import com.matchscribe.matchscribe_backend.integration.sportsapi.SportsApiClient;
import com.matchscribe.matchscribe_backend.repository.LeagueRepository;
import com.matchscribe.matchscribe_backend.repository.MatchRepository;
import com.matchscribe.matchscribe_backend.repository.SeriesRepository;
import com.matchscribe.matchscribe_backend.repository.TeamRepository;
import com.matchscribe.matchscribe_backend.repository.*;
import com.matchscribe.matchscribe_backend.service.MatchService;

import lombok.RequiredArgsConstructor;

@Service
public class MatchServiceImpl implements MatchService {
	
    private final SportsApiClient sportsApiClient;
    private final ObjectMapper objectMapper;

    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;
    private final VenueRepository venueRepository;
    private final MatchRepository matchRepository;
    private final SeriesRepository seriesRepository;
    
    public MatchServiceImpl(SportsApiClient sportsApiClient, ObjectMapper objectMapper, LeagueRepository leagueRepository,
    					TeamRepository teamRepository, VenueRepository venueRepository, MatchRepository matchRepository,
    					SeriesRepository seriesRepository) {
    			this.sportsApiClient = sportsApiClient;
    			this.objectMapper = objectMapper;
    			this.leagueRepository = leagueRepository;
    			this.teamRepository = teamRepository;
    			this.venueRepository = venueRepository;
    			this.matchRepository = matchRepository;
    			this.seriesRepository = seriesRepository;
    }

    @Override
    @Transactional
    public void importUpcomingMatchesFromSportsApi() {
        String json = sportsApiClient.getUpcomingMatches();
        System.out.println("Importing matches from Sports API..."+ json);
        

        try {
            JsonNode root = objectMapper.readTree(json);

            // Root -> typeMatches[]
            JsonNode typeMatches = root.path("typeMatches");
            if (!typeMatches.isArray()) {
                return; // nothing to process
            }

            for (JsonNode typeMatchNode : typeMatches) {
                // typeMatchNode -> seriesMatches[]
                JsonNode seriesMatches = typeMatchNode.path("seriesMatches");
                if (!seriesMatches.isArray()) {
                    continue;
                }
                String matchType = typeMatchNode.path("matchType").asText();
                if(matchType=="" || matchType==null) {
					continue;
					
				}
                Optional<League> matchTypeExists = leagueRepository.findByName(matchType);
                if (matchTypeExists.isEmpty()) {
                	League league = new League();
                	league.setName(matchType);
                	league.setSportId(1L); 
                	System.out.println("league: " + league);
                	
                	leagueRepository.saveAndFlush(league);
				}
                //getsl of saved league
                matchTypeExists = leagueRepository.findByName(matchType);
                
                for (JsonNode seriesMatch : seriesMatches) {

                    // 1) SERIES
                	JsonNode seriesId = seriesMatch.path("seriesAdWrapper").path("seriesId");
                	if (seriesId == null || seriesId.isMissingNode() || seriesId.isNull()) {
                	    continue;
                	}

                	Optional<Series> seriesExists = seriesRepository.findBySeriesId(seriesId.asLong());

                	if (seriesExists.isEmpty()) {
                	    Series series = new Series();
                	    series.setLeagueSl(matchTypeExists.get().getSl());
                	    series.setSeriesId(seriesId.asLong());

                	    JsonNode name = seriesMatch.path("seriesName");
                	    series.setSeriesName(name.asText());

                	    series.setInsertBy(LocalDateTime.now());

                	    seriesRepository.saveAndFlush(series);
                	}
                	seriesExists = seriesRepository.findBySeriesId(seriesId.asLong());


                    // 2) MATCHES INSIDE SERIES
                    JsonNode matchesArray = seriesMatch.path("seriesAdWrapper").path("matches");
                    if (!matchesArray.isArray()) {
                        continue;
                    }

                    for (JsonNode matchNode : matchesArray) {

                        // 2.1) VENUE
                        JsonNode venueNode = matchNode.path("matchInfo").path("venueInfo");
                        JsonNode venueKey = venueNode.path("id");
                            Optional<Venue> venueExists = venueRepository.findById(venueKey.asLong());
                            if (venueExists.isEmpty()) {
                                Venue venue = new Venue();
                                venue.setVenueId(venueKey.asLong()); // make sure this field exists
                                venue.setGround(venueNode.path("ground").asText());
                                venue.setCity(venueNode.path("city").asText());
                                venue.setCountry(venueNode.path("country").asText());
                                venueRepository.saveAndFlush(venue);
                            }
                        venueExists = venueRepository.findByVenueId(venueKey.asLong());

                         //2.2) TEAM 1
                        JsonNode team1Node = matchNode.path("matchInfo").path("team1");
                        Long team1Key = team1Node.path("teamId").asLong();
                            Optional<Team> team1Exists = teamRepository.findByTeamId(team1Key);
                            if (team1Exists.isEmpty()) {
                                Team team1 = new Team();
                                team1.setTeamId(team1Key); // make sure this field exists
                                team1.setTeamName(team1Node.path("teamName").asText());
                                team1.setTeamSName(team1Node.path("teamSName").asText());
                                teamRepository.saveAndFlush(team1);
                            }
                        team1Exists = teamRepository.findByTeamId(team1Key);

                        // 2.3) TEAM 2
                        JsonNode team2Node = matchNode.path("matchInfo").path("team2");
                        Long team2Key = team2Node.path("teamId").asLong();
                            Optional<Team> team2Exists = teamRepository.findByTeamId(team2Key);
                            if (team2Exists.isEmpty()) {
                                Team team2 = new Team();
                                team2.setTeamId(team2Key);
                                team2.setTeamName(team2Node.path("teamName").asText());
                                team2.setTeamSName(team2Node.path("teamSName").asText());
                                teamRepository.saveAndFlush(team2);
                            }
						team2Exists = teamRepository.findByTeamId(team2Key);
                        // 2.4) MATCH
                            JsonNode matches = matchNode.path("matchInfo");
                        Optional<Match> matchExists = matchRepository.findByMatchId(matches.path("matchId").asLong());
                        if (matchExists.isEmpty()) {
                            Match match = new Match();
                            match.setSportId(1L); // Long, not int
                            match.setSeriesId(seriesExists.get().getSl());
                            match.setVenueId(venueExists.get().getSl());
                            match.setHomeTeamId(team1Exists.get().getSl());
                            match.setAwayTeamId(team2Exists.get().getSl());
                            OffsetDateTime matchDateStr = epochToHuman(matches.path("startDate").asLong());
                            match.setStartDatetime(matchDateStr);
                            match.setEndDatetime(epochToHuman(matches.path("endDate").asLong()));
                            match.setMatchDesc(matchNode.path("matchDesc").asText());
                            match.setMatchId(matches.path("matchId").asLong());
                            match.setStatus(MatchStatus.scheduled.toString());
                            match.setResultType(MatchResultType.unknown.toString());
                            //print complete data instead of match
                            System.out.println("MATCH JSON:\n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(match));

                            matchRepository.saveAndFlush(match);
                        }
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse sports API response", e);
        }
    }
    
    public static OffsetDateTime epochToHuman(long epochMillis) {
        Instant instant = Instant.ofEpochMilli(epochMillis);
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
		return zdt.toOffsetDateTime();
    }
     @Override
    @Transactional(readOnly = true)
    public MatchDto getMatchById(Long matchId) {
        Optional<Match> optMatch = matchRepository.findByMatchId(matchId);
        if (!optMatch.isPresent()) {
            return null;
        }

        Match match = optMatch.get();
        MatchDto dto = new MatchDto();
        dto.match = match;

        // series (note: your Match entity stores SeriesId as Long)
        Long seriesId = match.getSeriesId();
        if (seriesId != null) {
            Optional<Series> optSeries = seriesRepository.findBySeriesId(seriesId);
            optSeries.ifPresent(s -> dto.series = s);
        }

        // teams
        Long homeTeamId = match.getHomeTeamId();
        if (homeTeamId != null) {
            Optional<Team> optHome = teamRepository.findByTeamId(homeTeamId);
            optHome.ifPresent(t -> dto.homeTeam = t);
        }

     // away team
        Long awayTeamId = match.getAwayTeamId();
        if (awayTeamId != null) {
            Optional<Team> optAway = teamRepository.findByTeamId(awayTeamId);
            optAway.ifPresent(t -> dto.awayTeam = t);
        }


        // venue
        Long venueId = match.getVenueId();
        if (venueId != null) {
            Optional<Venue> optVenue = venueRepository.findById(venueId);
            optVenue.ifPresent(v -> dto.venue = v);
        }

        return dto;
    }
     @Override
     @Transactional(readOnly = true)
     public Iterable<MatchDto> getAllMatches() {
		 Iterable<Match> matches = matchRepository.findAll();
		 List<MatchDto> dtos = new ArrayList<>();
		 for (Match match : matches) {
			 MatchDto dto = new MatchDto();
			 dto.match = match;

			 // series
			 Long seriesId = match.getSeriesId();
			 if (seriesId != null) {
				 Optional<Series> optSeries = seriesRepository.findBySeriesId(seriesId);
				 optSeries.ifPresent(s -> dto.series = s);
			 }

			 // home team
			 Long homeTeamId = match.getHomeTeamId();
			 if (homeTeamId != null) {
				 Optional<Team> optHome = teamRepository.findByTeamId(homeTeamId);
				 optHome.ifPresent(t -> dto.homeTeam = t);
			 }

			 // away team
			 Long awayTeamId = match.getAwayTeamId();
			 if (awayTeamId != null) {
				 Optional<Team> optAway = teamRepository.findByTeamId(awayTeamId);
				 optAway.ifPresent(t -> dto.awayTeam = t);
			 }

			 // venue
			 Long venueId = match.getVenueId();
			 if (venueId != null) {
				 Optional<Venue> optVenue = venueRepository.findById(venueId);
				 optVenue.ifPresent(v -> dto.venue = v);
			 }

			 dtos.add(dto);
		 }
		 return dtos;
	 }
}
