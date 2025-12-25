package com.matchscribe.matchscribe_backend.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "venue_stats")
public class VenueStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 @Column(name = "venue_sl", nullable = false)
    private long venueSl;

    // test | odi | t20 (or null if generic)
    @Column(name = "format", length = 20)
    private String format;

    // Example: "Total Matches", "Highest total recorded"
    @Column(name = "key", length = 150, nullable = false)
    private String key;

    // API values — allow multi-line
    @Column(name = "value", columnDefinition = "TEXT")
    private String value;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public VenueStats() {}

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }

    // ---- Getters & Setters ----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getVenueSl() { return venueSl; }
    public void setVenueSl(Long venueSl) { this.venueSl = venueSl; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
