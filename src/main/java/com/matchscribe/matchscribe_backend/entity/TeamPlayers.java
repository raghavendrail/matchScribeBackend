package com.matchscribe.matchscribe_backend.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "team_players", uniqueConstraints = {
		@UniqueConstraint(name = "uk_team_player", columnNames = { "team_sl", "player_sl" }) })
public class TeamPlayers {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sl;

	/* ---------- Relations ---------- */

	@JoinColumn(name = "team_sl", nullable = false, foreignKey = @ForeignKey(name = "fk_team_players_team"))
	private Long teamSl;

	@JoinColumn(name = "player_sl", nullable = false, foreignKey = @ForeignKey(name = "fk_team_players_player"))
	private Long playerSl;

	/* ---------- Fields ---------- */

	@Column(name = "role", length = 50)
	private String role; // BATSMAN, BOWLER, ALL_ROUNDER, WK

	@Column(name = "is_active")
	private Boolean isActive = true;

	@Column(name = "created_at", updatable = false)
	private OffsetDateTime createdAt;

	/* ---------- Lifecycle Hooks ---------- */

	@PrePersist
	protected void onCreate() {
		this.createdAt = OffsetDateTime.now();
	}

	/* ---------- Getters & Setters ---------- */

	public Long getSl() {
		return sl;
	}

	public Long getTeamSl() {
		return teamSl;
	}

	public void setTeamSl(Long teamSl) {
		this.teamSl = teamSl;
	}

	public Long getPlayerSl() {
		return playerSl;
	}

	public void setPlayerSl(Long playerSl) {
		this.playerSl = playerSl;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean active) {
		isActive = active;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}
}
