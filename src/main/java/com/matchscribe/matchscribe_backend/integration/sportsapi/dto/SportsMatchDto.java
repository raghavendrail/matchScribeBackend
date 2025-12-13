package com.matchscribe.matchscribe_backend.integration.sportsapi.dto;

public class SportsMatchDto {

    private Long externalMatchId;
    private String matchType;
    private String seriesName;
    private String matchDesc;
    private String matchFormat;
    private String statusText;
    private String state;
    private Long startDateEpochMs;
    private Long endDateEpochMs;

    private String team1Name;
    private String team1ShortName;
    private Integer team1Id;

    private String team2Name;
    private String team2ShortName;
    private Integer team2Id;

    private String venueName;
    private String city;

    // -----------------------
    // Getters and Setters
    // -----------------------

    public Long getExternalMatchId() {
        return externalMatchId;
    }

    public void setExternalMatchId(Long externalMatchId) {
        this.externalMatchId = externalMatchId;
    }
    public String getMatchType() {
		return matchType;
	}
    	public void setMatchType(String matchType) {
    				this.matchType = matchType;
    	}

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getMatchDesc() {
        return matchDesc;
    }

    public void setMatchDesc(String matchDesc) {
        this.matchDesc = matchDesc;
    }

    public String getMatchFormat() {
        return matchFormat;
    }

    public void setMatchFormat(String matchFormat) {
        this.matchFormat = matchFormat;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getStartDateEpochMs() {
        return startDateEpochMs;
    }

    public void setStartDateEpochMs(Long startDateEpochMs) {
        this.startDateEpochMs = startDateEpochMs;
    }

    public Long getEndDateEpochMs() {
        return endDateEpochMs;
    }

    public void setEndDateEpochMs(Long endDateEpochMs) {
        this.endDateEpochMs = endDateEpochMs;
    }

    public String getTeam1Name() {
        return team1Name;
    }

    public void setTeam1Name(String team1Name) {
        this.team1Name = team1Name;
    }

    public String getTeam1ShortName() {
        return team1ShortName;
    }

    public void setTeam1ShortName(String team1ShortName) {
        this.team1ShortName = team1ShortName;
    }

    public Integer getTeam1Id() {
        return team1Id;
    }

    public void setTeam1Id(Integer team1Id) {
        this.team1Id = team1Id;
    }

    public String getTeam2Name() {
        return team2Name;
    }

    public void setTeam2Name(String team2Name) {
        this.team2Name = team2Name;
    }

    public String getTeam2ShortName() {
        return team2ShortName;
    }

    public void setTeam2ShortName(String team2ShortName) {
        this.team2ShortName = team2ShortName;
    }

    public Integer getTeam2Id() {
        return team2Id;
    }

    public void setTeam2Id(Integer team2Id) {
        this.team2Id = team2Id;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
