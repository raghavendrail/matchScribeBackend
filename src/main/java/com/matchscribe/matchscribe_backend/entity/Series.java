package com.matchscribe.matchscribe_backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "series")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Series {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sl")
	private Long sl;

	// league_sl (FK → leagues.sl)
	@Column(name = "league_sl")
	private Long leagueSl;

	@Column(name = "series_id", nullable = false)
	private Long seriesId;

	@Column(name = "series_name", length = 200)
	private String seriesName;
	@Column(name = "slug", length = 200)
	private String slug;

	@Column(name = "insert_by")
	private LocalDateTime insertBy;

	@Column(name = "update_by")
	private LocalDateTime updateBy;

	// getter and setter
	public Long getSl() {
		return sl;
	}

	public void setSl(Long sl) {
		this.sl = sl;

	}

	public Long getLeagueSl() {
		return leagueSl;
	}

	public void setLeagueSl(Long leagueSl) {
		this.leagueSl = leagueSl;
	}

	public Long getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(Long seriesId) {
		this.seriesId = seriesId;
	}

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public LocalDateTime getInsertBy() {
		return insertBy;

	}

	public void setInsertBy(LocalDateTime insertBy) {
		this.insertBy = insertBy;
	}

	public LocalDateTime getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(LocalDateTime updateBy) {
		this.updateBy = updateBy;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	@PrePersist
	protected void onCreate() {
		insertBy = LocalDateTime.now();
		updateBy = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updateBy = LocalDateTime.now();
	}
}
