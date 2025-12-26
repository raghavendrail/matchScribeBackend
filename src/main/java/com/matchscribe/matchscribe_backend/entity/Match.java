package com.matchscribe.matchscribe_backend.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.matchscribe.matchscribe_backend.entity.enums.MatchResultType;
import com.matchscribe.matchscribe_backend.entity.enums.MatchStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Entity
@Table(name = "matches", schema = "public")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "matches_seq")
    @SequenceGenerator(name = "matches_seq", sequenceName = "matches_id_seq", allocationSize = 1)
    private Long sl;

    @Column(name = "sport_id", nullable = false)
    private Long sportId;

    @Column(name = "series_id")
    private Long seriesId;

    @Column(name = "venue_id")
    private Long venueId;

    @Column(name = "home_team_id", nullable = false)
    private Long homeTeamId;

    @Column(name = "away_team_id", nullable = false)
    private Long awayTeamId;

    // DB: match_id integer
    @Column(name = "match_id")
    private Long matchId;
    
    @Column(name="format_sl")
  
    private Long formatSl;

    // DB: match_desc character varying(100)
    @Column(name = "match_desc", length = 100)
    private String matchDesc;

    @Column(name = "slug", nullable = false, length = 200, unique = true)
    private String slug;

    @Column(name = "short_title", nullable = false, length = 200)
    private String shortTitle;

    @Column(name = "long_title", length = 300)
    private String longTitle;

    @Column(name = "start_datetime", nullable = false)
    private OffsetDateTime startDatetime;

    @Column(name = "end_datetime")
    private OffsetDateTime endDatetime;


    @Column(name = "status")
    private String status;

    @Column(name = "result_type")
    private String resultType;

    @Column(name = "winner_team_id")
    private Long winnerTeamId;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "keywords", columnDefinition = "text[]")
    private String[] keywords;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "tags_cached", columnDefinition = "text[]")
    private String[] tagsCached;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "extra_info", columnDefinition = "jsonb")
    private JsonNode extraInfo;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "probable_xi_home", columnDefinition = "text[]")
    private String[] probableXiHome;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "probable_xi_away", columnDefinition = "text[]")
    private String[] probableXiAway;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "final_xi_home", columnDefinition = "text[]")
    private String[] finalXiHome;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "final_xi_away", columnDefinition = "text[]")
    private String[] finalXiAway;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
    

    // ---------------------------
    // Constructors
    // ---------------------------
    public Match() {}

    // (constructor omitted for brevity — you can keep or generate as needed)

    // ---------------------------
    // Getters and Setters
    // ---------------------------

    public Long getSl() { return sl; }
    public void setSl(Long sl) { this.sl = sl; }

    public Long getSportId() { return sportId; }
    public void setSportId(Long sportId) { this.sportId = sportId; }

    public Long getSeriesId() { return seriesId; }
    public void setSeriesId(Long seriesId) { this.seriesId = seriesId; }

    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }

    public Long getHomeTeamId() { return homeTeamId; }
    public void setHomeTeamId(Long homeTeamId) { this.homeTeamId = homeTeamId; }

    public Long getAwayTeamId() { return awayTeamId; }
    public void setAwayTeamId(Long awayTeamId) { this.awayTeamId = awayTeamId; }

    public Long getMatchId() { return matchId; }
    public void setMatchId(Long matchId) { this.matchId = matchId; }
    public Long getFormatSl() { return formatSl; }
    public void setFormatSl(Long formatSl) { this.formatSl = formatSl; }

    public String getMatchDesc() { return matchDesc; }
    public void setMatchDesc(String matchDesc) { this.matchDesc = matchDesc; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getShortTitle() { return shortTitle; }
    public void setShortTitle(String shortTitle) { this.shortTitle = shortTitle; }

    public String getLongTitle() { return longTitle; }
    public void setLongTitle(String longTitle) { this.longTitle = longTitle; }

    public OffsetDateTime getStartDatetime() { return startDatetime; }
    public void setStartDatetime(OffsetDateTime startDatetime) { this.startDatetime = startDatetime; }

    public OffsetDateTime getEndDatetime() { return endDatetime; }
    public void setEndDatetime(OffsetDateTime endDatetime) { this.endDatetime = endDatetime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getResultType() { return resultType; }
    public void setResultType(String resultType) { this.resultType = resultType; }

    public Long getWinnerTeamId() { return winnerTeamId; }
    public void setWinnerTeamId(Long winnerTeamId) { this.winnerTeamId = winnerTeamId; }

    public String[] getKeywords() { return keywords; }
    public void setKeywords(String[] keywords) { this.keywords = keywords; }

    public String[] getTagsCached() { return tagsCached; }
    public void setTagsCached(String[] tagsCached) { this.tagsCached = tagsCached; }

    public JsonNode getExtraInfo() { return extraInfo; }
    public void setExtraInfo(JsonNode extraInfo) { this.extraInfo = extraInfo; }

    public String[] getProbableXiHome() { return probableXiHome; }
    public void setProbableXiHome(String[] probableXiHome) { this.probableXiHome = probableXiHome; }

    public String[] getProbableXiAway() { return probableXiAway; }
    public void setProbableXiAway(String[] probableXiAway) { this.probableXiAway = probableXiAway; }

    public String[] getFinalXiHome() { return finalXiHome; }
    public void setFinalXiHome(String[] finalXiHome) { this.finalXiHome = finalXiHome; }

    public String[] getFinalXiAway() { return finalXiAway; }
    public void setFinalXiAway(String[] finalXiAway) { this.finalXiAway = finalXiAway; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Lifecycle hooks
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
