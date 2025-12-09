package com.matchscribe.matchscribe_backend.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.matchscribe.matchscribe_backend.entity.enums.MatchResultType;
import com.matchscribe.matchscribe_backend.entity.enums.MatchStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sport_id", nullable = false)
    private Long sportId;

    @Column(name = "league_id")
    private Long leagueId;

    @Column(name = "venue_id")
    private Long venueId;

    @Column(name = "home_team_id", nullable = false)
    private Long homeTeamId;

    @Column(name = "away_team_id", nullable = false)
    private Long awayTeamId;

    @Column(name = "match_key", nullable = false, length = 100)
    private String matchKey;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "match_status")
    private MatchStatus status = MatchStatus.scheduled;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_type", nullable = false, columnDefinition = "match_result_type")
    private MatchResultType resultType = MatchResultType.unknown;

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

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
        if (status == null) {
            status = MatchStatus.scheduled;
        }
        if (resultType == null) {
            resultType = MatchResultType.unknown;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}