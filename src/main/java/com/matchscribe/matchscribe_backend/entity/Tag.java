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
@Table(name = "tags", uniqueConstraints = { @UniqueConstraint(name = "uk_tags_name", columnNames = "name"),
		@UniqueConstraint(name = "uk_tags_slug", columnNames = "slug") })
public class Tag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sl")
	private Long sl;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "slug", nullable = false, length = 100)
	private String slug;

	@Column(name = "tag_type", length = 50)
	private String tagType;

	@Column(name = "created_at", nullable = false, updatable = false)
	private OffsetDateTime createdAt = OffsetDateTime.now();

	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt = OffsetDateTime.now();

	@Column(name = "usage_count", nullable = false)
	private Integer usageCount = 0;

	@Column(name = "is_active")
	private Boolean isActive = true;

	// -------- getters & setters --------

	public Long getSl() {
		return sl;
	}

	public void setSl(Long sl) {
		this.sl = sl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
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

	public Integer getUsageCount() {
		return usageCount;
	}

	public void setUsageCount(Integer usageCount) {
		this.usageCount = usageCount;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean active) {
		isActive = active;
	}
}
