package com.matchscribe.matchscribe_backend.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "series_content")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeriesContent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Relation
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "series_sl", nullable = false)
	private long seriesSl;

	@Column(name = "series_slug", length = 200, nullable = false, unique = true)
	private String seriesSlug;

	// Core sections
	@Column(columnDefinition = "TEXT", nullable = false)
	private String overview;

	@Column(name = "playing_style", columnDefinition = "TEXT")
	private String playingStyle;

	@Column(name = "why_this_series_matters", columnDefinition = "TEXT")
	private String whyThisSeriesMatters;

	// Lists / scannable
	@Column(name = "key_highlights", columnDefinition = "jsonb")
	private String keyHighlights;

	@Column(name = "fan_experience", columnDefinition = "TEXT")
	private String fanExperience;

	@Column(name = "talent_impact", columnDefinition = "TEXT")
	private String talentImpact;

	@Column(name = "global_relevance", columnDefinition = "TEXT")
	private String globalRelevance;

	// Engagement boosters
	@Column(name = "hook_line", length = 300)
	private String hookLine;

	@Column(columnDefinition = "TEXT")
	private String conclusion;

	@Column(name = "reading_time_minutes")
	private Short readingTimeMinutes;

	// Metadata
	@Column(length = 10)
	private String language = "en";

	@Column(name = "content_version")
	private Integer contentVersion = 1;

	@Column(name = "is_active")
	private Boolean isActive = true;

	// Audit
	@Column(name = "created_at")
	private OffsetDateTime createdAt;

	@Column(name = "updated_at")
	private OffsetDateTime updatedAt;

	// getter and setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSeriesSl() {
		return seriesSl;
	}

	public void setSeriesSl(Long seriesSl) {
		this.seriesSl = seriesSl;
	}

	public String getSeriesSlug() {
		return seriesSlug;
	}

	public void setSeriesSlug(String seriesSlug) {
		this.seriesSlug = seriesSlug;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getPlayingStyle() {
		return playingStyle;
	}

	public void setPlayingStyle(String playingStyle) {
		this.playingStyle = playingStyle;
	}

	public String getWhyThisSeriesMatters() {
		return whyThisSeriesMatters;
	}

	public void setWhyThisSeriesMatters(String whyThisSeriesMatters) {
		this.whyThisSeriesMatters = whyThisSeriesMatters;
	}

	public String getKeyHighlights() {
		return keyHighlights;
	}

	public void setKeyHighlights(String keyHighlights) {
		this.keyHighlights = keyHighlights;
	}

	public String getFanExperience() {
		return fanExperience;
	}

	public void setFanExperience(String fanExperience) {
		this.fanExperience = fanExperience;
	}

	public String getTalentImpact() {
		return talentImpact;
	}

	public void setTalentImpact(String talentImpact) {
		this.talentImpact = talentImpact;
	}

	public String getGlobalRelevance() {
		return globalRelevance;
	}

	public void setGlobalRelevance(String globalRelevance) {
		this.globalRelevance = globalRelevance;
	}

	public String getHookLine() {
		return hookLine;
	}

	public void setHookLine(String hookLine) {
		this.hookLine = hookLine;
	}

	public String getConclusion() {
		return conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public Short getReadingTimeMinutes() {
		return readingTimeMinutes;
	}

	public void setReadingTimeMinutes(Short readingTimeMinutes) {
		this.readingTimeMinutes = readingTimeMinutes;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getContentVersion() {
		return contentVersion;
	}

	public void setContentVersion(Integer contentVersion) {
		this.contentVersion = contentVersion;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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

	@PrePersist
	protected void onCreate() {
		createdAt = OffsetDateTime.now();
		updatedAt = createdAt;
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = OffsetDateTime.now();
	}
}
