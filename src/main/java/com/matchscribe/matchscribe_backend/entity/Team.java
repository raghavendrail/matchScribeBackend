package com.matchscribe.matchscribe_backend.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "teams",
		// no extra uniqueConstraints here because slug is annotated unique at column
		// level
		// (SQL has uk_teams_slug UNIQUE (slug))
		schema = "public")
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teams_seq")
	@SequenceGenerator(name = "teams_seq", sequenceName = "teams_id_seq", allocationSize = 1)
	private Long sl;

	@Column(name = "league_sl")
	private Long leagueSl; // maps to bigint (nullable in DB)

	@Column(name = "team_id")
	private Long teamId; // maps to integer

	@Column(name = "team_name", length = 150)
	private String teamName;

	@Column(name = "team_s_name", length = 200)
	private String teamSName;

	@Column(name = "slug", length = 150, unique = true)
	private String slug;

	@Column(name = "country", length = 100)
	private String country;

	@Column(name = "is_international")
	private Boolean isInternational = false;

	@Column(name = "created_at", columnDefinition = "timestamp with time zone")
	private OffsetDateTime createdAt;

	@Column(name = "updated_at", columnDefinition = "timestamp with time zone")
	private OffsetDateTime updatedAt;

	// -----------------------------------------------------
	// Constructors
	// -----------------------------------------------------

	public Team() {
	}

	public Team(Long sl, Long teamId, Long leagueSl, String teamName, String teamSName, String slug, String country,
			Boolean isInternational, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
		this.sl = sl;
		this.teamId = teamId;
		this.leagueSl = leagueSl;
		this.teamName = teamName;
		this.teamSName = teamSName;
		this.slug = slug;
		this.country = country;
		this.isInternational = isInternational;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	// -----------------------------------------------------
	// Getters & Setters
	// -----------------------------------------------------

	public Long getSl() {
		return sl;
	}

	public void setSl(Long sl) {
		this.sl = sl;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public Long getLeagueSl() {
		return leagueSl;
	}

	public void setLeagueSl(Long sportId) {
		this.leagueSl = leagueSl;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamSName() {
		return teamSName;
	}

	public void setTeamSName(String teamSName) {
		this.teamSName = teamSName;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Boolean getIsInternational() {
		return isInternational;
	}

	public void setIsInternational(Boolean isInternational) {
		this.isInternational = isInternational;
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

	// -----------------------------------------------------
	// Lifecycle Hooks
	// -----------------------------------------------------

	@PrePersist
	protected void onCreate() {
		if (createdAt == null) {
			createdAt = OffsetDateTime.now();
		}
		updatedAt = OffsetDateTime.now();
		if (isInternational == null) {
			isInternational = false;
		}
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = OffsetDateTime.now();
	}
}
