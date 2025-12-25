package com.matchscribe.matchscribe_backend.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "venues", uniqueConstraints = { @UniqueConstraint(columnNames = { "venue_id" }),
		@UniqueConstraint(columnNames = { "slug" }) })
public class Venue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sl;

	@Column(name = "venue_id", nullable = false)
	private Long venueId;

	@Column(name = "ground", nullable = false, length = 200)
	private String ground;

	@Column(name = "city", length = 120)
	private String city;

	@Column(name = "country", length = 120)
	private String country;

	@Column(name = "slug", nullable = false, length = 220)
	private String slug;

	// ---- Stadium details ----
	@Column(name = "timezone", length = 20)
	private String timezone;

	@Column(name = "capacity")
	private Integer capacity;

	@Column(name = "ends", length = 255)
	private String ends;

	@Column(name = "home_team", length = 150)
	private String homeTeam;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "image_id", length = 50)
	private String imageId;

	@Column(name = "established_year")
	private Short establishedYear;

	@Column(name = "pitch_type", length = 50)
	private String pitchType; // batting / bowling / balanced / unknown

	@Column(name = "description")
	private String description;

	// ---- Geo ----
	@Column(name = "latitude", precision = 9, scale = 6)
	private Double latitude;

	@Column(name = "longitude", precision = 9, scale = 6)
	private Double longitude;

	// ---- Status ----
	@Column(name = "is_active")
	private Boolean isActive = true;

	// ---- Timestamps ----
	@Column(name = "created_at", nullable = false, updatable = false)
	private OffsetDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;

	// ----- Constructors -----

	public Venue() {
	}

	// (Optional) constructor if you want — can be customized
	public Venue(Long venueId, String ground, String slug) {
		this.venueId = venueId;
		this.ground = ground;
		this.slug = slug;
	}

	// ----- Getters & Setters -----
	public Long getSl() {
		return sl;
	}

	public void setSl(Long sl) {
		this.sl = sl;
	}

	public Long getVenueId() {
		return venueId;
	}
	public void setVenueId(Long venueId) {
		this.venueId = venueId;
	}
	public String getGround() {
		return ground;
	}
	public void setGround(String ground) {
		this.ground = ground;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public Integer getCapacity() {
		return capacity;
	}
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	public String getEnds() {
		return ends;
	}
	public void setEnds(String ends) {
		this.ends = ends;
	}
	public String getHomeTeam() {
		return homeTeam;
	}
	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public Short getEstablishedYear() {
		return establishedYear;
	}
	public void setEstablishedYear(Short establishedYear) {
		this.establishedYear = establishedYear;
	}
	public String getPitchType() {
		return pitchType;
	}
	public void setPitchType(String pitchType) {
		this.pitchType = pitchType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
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
	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
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
