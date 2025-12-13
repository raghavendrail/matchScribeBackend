package com.matchscribe.matchscribe_backend.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "venues")
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sl;
    @Column(name = "venue_id", nullable = false, unique = true)
    private Long venueId;

    @Column(name = "ground", nullable = false, length = 200)
    private String ground;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "slug", nullable = false, length = 200, unique = true)
    private String slug;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    // -----------------------------------------------------
    // Constructors
    // -----------------------------------------------------

    public Venue() {}

    public Venue(Long sl, Long venueId, String ground, String city, String country,
                 String slug, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
    			this.sl = sl;

        this.venueId = venueId;
        this.ground = ground;
        this.city = city;
        this.country = country;
        this.slug = slug;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // -----------------------------------------------------
    // Getters & Setters
    // -----------------------------------------------------
    public Long getSl() { return sl; }
    public void setSl(Long sl) { this.sl = sl; }

    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }

    public String getGround() { return ground; }
    public void setGround(String ground) { this.ground = ground; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    // -----------------------------------------------------
    // Lifecycle Hooks
    // -----------------------------------------------------

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
