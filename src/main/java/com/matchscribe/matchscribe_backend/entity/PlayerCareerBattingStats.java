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
@Table(name = "player_career_batting_stats", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "player_sl", "match_type" }) })
public class PlayerCareerBattingStats {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sl;

	@Column(name = "player_sl", nullable = false)
	private Long playerSl;

	@Column(name = "match_type", length = 20, nullable = false)
	private String matchType; // Test, ODI, T20, IPL

	private Integer matches;
	private Integer innings;
	private Integer runs;
	private Integer balls;

	@Column(name = "highest_score")
	private Integer highestScore;

	@Column(name = "average")
	private Double average;

	@Column(name = "strike_rate")
	private Double strikeRate;

	@Column(name = "not_outs")
	private Integer notOuts;

	private Integer fours;
	private Integer sixes;
	private Integer ducks;

	private Integer fifties;
	private Integer hundreds;

	@Column(name = "double_hundreds")
	private Integer doubleHundreds;

	@Column(name = "triple_hundreds")
	private Integer tripleHundreds;

	@Column(name = "quadruple_hundreds")
	private Integer quadrupleHundreds;

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

	public Integer getRuns() {
		return runs;
	}

	public void setRuns(Integer runs) {
		this.runs = runs;
	}

	public Integer getBalls() {
		return balls;
	}

	public void setBalls(Integer balls) {
		this.balls = balls;
	}

	public Integer getHighestScore() {
		return highestScore;
	}

	public void setHighestScore(Integer highestScore) {
		this.highestScore = highestScore;
	}

	public Double getAverage() {
		return average;
	}

	public void setAverage(Double average) {
		this.average = average;
	}

	public Double getStrikeRate() {
		return strikeRate;
	}

	public void setStrikeRate(Double strikeRate) {
		this.strikeRate = strikeRate;
	}

	public Integer getNotOuts() {
		return notOuts;
	}

	public void setNotOuts(Integer notOuts) {
		this.notOuts = notOuts;
	}

	public Integer getFours() {
		return fours;
	}

	public void setFours(Integer fours) {
		this.fours = fours;
	}

	public Integer getSixes() {
		return sixes;
	}

	public void setSixes(Integer sixes) {
		this.sixes = sixes;
	}

	public Integer getDucks() {
		return ducks;
	}

	public void setDucks(Integer ducks) {
		this.ducks = ducks;
	}

	public Integer getFifties() {
		return fifties;
	}

	public void setFifties(Integer fifties) {
		this.fifties = fifties;
	}

	public Integer getHundreds() {
		return hundreds;
	}

	public void setHundreds(Integer hundreds) {
		this.hundreds = hundreds;
	}

	public Integer getDoubleHundreds() {
		return doubleHundreds;
	}

	public void setDoubleHundreds(Integer doubleHundreds) {
		this.doubleHundreds = doubleHundreds;
	}

	public Integer getTripleHundreds() {
		return tripleHundreds;
	}

	public void setTripleHundreds(Integer tripleHundreds) {
		this.tripleHundreds = tripleHundreds;
	}

	public Integer getQuadrupleHundreds() {
		return quadrupleHundreds;
	}

	public void setQuadrupleHundreds(Integer quadrupleHundreds) {
		this.quadrupleHundreds = quadrupleHundreds;
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
