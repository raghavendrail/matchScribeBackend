package com.matchscribe.matchscribe_backend.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "players", uniqueConstraints = {
		@UniqueConstraint(name = "uk_players_player_id", columnNames = "player_id") })
public class Players {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sl;

	@Column(name = "player_id", nullable = false)
	private Long playerId;

	@Column(name = "name", nullable = false, length = 150)
	private String name;

	@Column(name = "batting_style", length = 100)
	private String battingStyle;

	@Column(name = "bowling_style", length = 100)
	private String bowlingStyle;

	@Column(name = "created_at", updatable = false)
	private OffsetDateTime createdAt;

	@Column(name = "updated_at")
	private OffsetDateTime updatedAt;

	/* ---------- Lifecycle Hooks ---------- */

	@PrePersist
	protected void onCreate() {
		this.createdAt = OffsetDateTime.now();
		this.updatedAt = OffsetDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = OffsetDateTime.now();
	}

	/* ---------- Getters & Setters ---------- */

	public Long getSl() {
		return sl;
	}

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBattingStyle() {
		return battingStyle;
	}

	public void setBattingStyle(String battingStyle) {
		this.battingStyle = battingStyle;
	}

	public String getBowlingStyle() {
		return bowlingStyle;
	}

	public void setBowlingStyle(String bowlingStyle) {
		this.bowlingStyle = bowlingStyle;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
	}
}
