package com.matchscribe.matchscribe_backend.entity;

import java.time.OffsetDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;
import com.matchscribe.matchscribe_backend.entity.enums.PostCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

	// --------------------
	// Core Identifiers
	// --------------------
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "sport_id", nullable = false)
	private Long sportId;

	@Column(name = "match_id")
	private Long matchId;

	@Column(name = "league_id")
	private Long leagueId;

	// --------------------
	// SEO & Metadata
	// --------------------
	@Column(name = "title", nullable = false, length = 250)
	private String title;

	@Column(name = "slug", nullable = false, length = 250, unique = true)
	private String slug;

	@Column(name = "seo_title", length = 250)
	private String seoTitle;

	@Column(name = "seo_description", length = 300)
	private String seoDescription;

	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false, columnDefinition = "post_category")
	private PostCategory category = PostCategory.preview;

	@JdbcTypeCode(SqlTypes.ARRAY)
	@Column(name = "tag_slugs", columnDefinition = "text[]")
	private String[] tagSlugs = new String[] {};

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "meta", columnDefinition = "jsonb")
	private JsonNode meta;

	// --------------------
	// Match Detail Sections
	// --------------------
	@Column(name = "overview", columnDefinition = "TEXT")
	private String overview;

	@Column(name = "team_form", columnDefinition = "TEXT")
	private String teamForm;

	@Column(name = "pitch_report", columnDefinition = "TEXT")
	private String pitchReport;

	@Column(name = "weather", columnDefinition = "TEXT")
	private String weather;

	@JdbcTypeCode(SqlTypes.ARRAY)
	@Column(name = "home_probable_xi", columnDefinition = "integer[]")
	private Integer[] homeProbableXi;

	@JdbcTypeCode(SqlTypes.ARRAY)
	@Column(name = "away_probable_xi", columnDefinition = "integer[]")
	private Integer[] awayProbableXi;

	@Column(name = "key_players", columnDefinition = "TEXT")
	private String keyPlayers;

	@Column(name = "head_to_head", columnDefinition = "TEXT")
	private String headToHead;

	@Column(name = "prediction", columnDefinition = "TEXT")
	private String prediction;

	@Column(name = "fantasy_tips", columnDefinition = "TEXT")
	private String fantasyTips;

	@Column(name = "final_verdict", columnDefinition = "TEXT")
	private String finalVerdict;

	// --------------------
	// Publishing
	// --------------------
	@Column(name = "is_published", nullable = false)
	private Boolean isPublished = false;

	@Column(name = "published_at")
	private OffsetDateTime publishedAt;

	// --------------------
	// Audit Fields
	// --------------------
	@Column(name = "created_at", nullable = false, updatable = false)
	private OffsetDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;

	// getter and setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSportId() {
		return sportId;
	}

	public void setSportId(Long sportId) {
		this.sportId = sportId;
	}

	public Long getMatchId() {
		return matchId;
	}

	public void setMatchId(Long matchId) {
		this.matchId = matchId;
	}

	public Long getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(Long leagueId) {
		this.leagueId = leagueId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public PostCategory getCategory() {
		return category;
	}

	public void setCategory(PostCategory category) {
		this.category = category;
	}

	public String[] getTagSlugs() {
		return tagSlugs;
	}

	public void setTagSlugs(String[] tagSlugs) {
		this.tagSlugs = tagSlugs;
	}

	public JsonNode getMeta() {
		return meta;
	}

	public void setMeta(JsonNode meta) {
		this.meta = meta;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getTeamForm() {
		return teamForm;
	}

	public void setTeamForm(String teamForm) {
		this.teamForm = teamForm;
	}

	public String getPitchReport() {
		return pitchReport;
	}

	public void setPitchReport(String pitchReport) {
		this.pitchReport = pitchReport;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public Integer[] getHomeProbableXi() {
		return homeProbableXi;
	}

	public void setHomeProbableXi(Integer[] homeProbableXi) {
		this.homeProbableXi = homeProbableXi;
	}

	public Integer[] getAwayProbableXi() {
		return awayProbableXi;
	}

	public void setAwayProbableXi(Integer[] awayProbableXi) {
		this.awayProbableXi = awayProbableXi;
	}

	public String getKeyPlayers() {
		return keyPlayers;
	}

	public void setKeyPlayers(String keyPlayers) {
		this.keyPlayers = keyPlayers;
	}

	public String getHeadToHead() {
		return headToHead;
	}

	public void setHeadToHead(String headToHead) {
		this.headToHead = headToHead;
	}

	public String getPrediction() {
		return prediction;
	}

	public void setPrediction(String prediction) {
		this.prediction = prediction;
	}

	public String getFantasyTips() {
		return fantasyTips;
	}

	public void setFantasyTips(String fantasyTips) {
		this.fantasyTips = fantasyTips;
	}

	public String getFinalVerdict() {
		return finalVerdict;
	}

	public void setFinalVerdict(String finalVerdict) {
		this.finalVerdict = finalVerdict;
	}

	public Boolean getIsPublished() {
		return isPublished;
	}

	public void setIsPublished(Boolean isPublished) {
		this.isPublished = isPublished;
	}

	public OffsetDateTime getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(OffsetDateTime publishedAt) {
		this.publishedAt = publishedAt;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(OffsetDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	// --------------------
	// Lifecycle Hooks
	// --------------------
	@PrePersist
	protected void onCreate() {
		createdAt = OffsetDateTime.now();
		updatedAt = OffsetDateTime.now();
		if (category == null) {
			category = PostCategory.preview;
		}
		if (isPublished == null) {
			isPublished = false;
		}
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = OffsetDateTime.now();
	}
}
