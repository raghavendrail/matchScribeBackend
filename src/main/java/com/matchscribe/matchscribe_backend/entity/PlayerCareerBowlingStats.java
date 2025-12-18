package com.matchscribe.matchscribe_backend.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "player_career_bowling_stats", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "player_sl", "match_type" }) })
public class PlayerCareerBowlingStats {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sl;

	@Column(name = "player_sl", nullable = false)
	private Long playerSl;

	@Column(name = "match_type", length = 20, nullable = false)
	private String matchType; // Test, ODI, T20, IPL

	private Integer matches;
	private Integer innings;
	private Integer balls;
	private Integer runs;
	private Integer maidens;
	private Integer wickets;

	@Column(name = "average")
	private Double average;

	@Column(name = "economy")
	private Double economy;

	@Column(name = "strike_rate")
	private Double strikeRate;

	@Column(name = "best_bowling_innings", length = 10)
	private String bestBowlingInnings;

	@Column(name = "best_bowling_match", length = 10)
	private String bestBowlingMatch;

	@Column(name = "four_wickets")
	private Integer fourWickets;

	@Column(name = "five_wickets")
	private Integer fiveWickets;

	@Column(name = "ten_wickets")
	private Integer tenWickets;

	@Column(name = "created_at")
	private OffsetDateTime createdAt;

	@Column(name = "updated_at")
	private OffsetDateTime updatedAt;

	/* ================= GETTERS & SETTERS ================= */

	public Long getSl() {
		return sl;
	}

	public void setSl(Long sl) {
		this.sl = sl;
	}

	public Long getPlayerSl() {
		return playerSl;
	}

	public void setPlayerSl(Long playerSl) {
		this.playerSl = playerSl;
	}

	public String getMatchType() {
		return matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

	public Integer getMatches() {
		return matches;
	}

	public void setMatches(Integer matches) {
		this.matches = matches;
	}

	public Integer getInnings() {
		return innings;
	}

	public void setInnings(Integer innings) {
		this.innings = innings;
	}

	public Integer getBalls() {
		return balls;
	}

	public void setBalls(Integer balls) {
		this.balls = balls;
	}

	public Integer getRuns() {
		return runs;
	}

	public void setRuns(Integer runs) {
		this.runs = runs;
	}

	public Integer getMaidens() {
		return maidens;
	}

	public void setMaidens(Integer maidens) {
		this.maidens = maidens;
	}

	public Integer getWickets() {
		return wickets;
	}

	public void setWickets(Integer wickets) {
		this.wickets = wickets;
	}

	public Double getAverage() {
		return average;
	}

	public void setAverage(Double average) {
		this.average = average;
	}

	public Double getEconomy() {
		return economy;
	}

	public void setEconomy(Double economy) {
		this.economy = economy;
	}

	public Double getStrikeRate() {
		return strikeRate;
	}

	public void setStrikeRate(Double strikeRate) {
		this.strikeRate = strikeRate;
	}

	public String getBestBowlingInnings() {
		return bestBowlingInnings;
	}

	public void setBestBowlingInnings(String bestBowlingInnings) {
		this.bestBowlingInnings = bestBowlingInnings;
	}

	public String getBestBowlingMatch() {
		return bestBowlingMatch;
	}

	public void setBestBowlingMatch(String bestBowlingMatch) {
		this.bestBowlingMatch = bestBowlingMatch;
	}

	public Integer getFourWickets() {
		return fourWickets;
	}

	public void setFourWickets(Integer fourWickets) {
		this.fourWickets = fourWickets;
	}

	public Integer getFiveWickets() {
		return fiveWickets;
	}

	public void setFiveWickets(Integer fiveWickets) {
		this.fiveWickets = fiveWickets;
	}

	public Integer getTenWickets() {
		return tenWickets;
	}

	public void setTenWickets(Integer tenWickets) {
		this.tenWickets = tenWickets;
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
}
