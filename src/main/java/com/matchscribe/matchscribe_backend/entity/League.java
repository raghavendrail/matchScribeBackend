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

@Entity
@Table(name = "leagues")
public class League {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sl")
	private Long sl;

	@Column(name = "sport_id", nullable = false)
	private Long sportId;

	@Column(name = "name", nullable = false, length = 200)
	private String name;
	@Column(name = "slug", nullable = false, length = 200, unique = true)
	private String slug;

	@Column(name = "created_at", nullable = false, updatable = false)
	private OffsetDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;

	public League() {
	}

	// getters / setters — note changed method names
	public Long getSl() {
		return sl;
	}

	public void setSl(Long sl) {
		this.sl = sl;
	}

	public Long getSportId() {
		return sportId;
	}

	public void setSportId(Long sportId) {
		this.sportId = sportId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	@PrePersist
	protected void onCreate() {
		createdAt = OffsetDateTime.now();
		updatedAt = OffsetDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = OffsetDateTime.now();
	}
}
