package com.matchscribe.matchscribe_backend.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.matchscribe.matchscribe_backend.entity.enums.PostCategory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sport_id", nullable = false)
    private Long sportId;

    @Column(name = "match_id")
    private Long matchId;

    @Column(name = "league_id")
    private Long leagueId;

    @Column(name = "title", nullable = false, length = 250)
    private String title;

    @Column(name = "slug", nullable = false, length = 250, unique = true)
    private String slug;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "content_html", nullable = false, columnDefinition = "TEXT")
    private String contentHtml;

    @Column(name = "seo_title", length = 250)
    private String seoTitle;

    @Column(name = "seo_description", length = 300)
    private String seoDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, columnDefinition = "post_category")
    private PostCategory category = PostCategory.preview;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "tag_slugs", columnDefinition = "text[]")
    private String[] tagSlugs;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "meta", columnDefinition = "jsonb")
    private JsonNode meta;

    @Column(name = "is_published", nullable = false)
    private Boolean isPublished = false;

    @Column(name = "published_at")
    private OffsetDateTime publishedAt;

    @Column(name = "hero_image_url", length = 500)
    private String heroImageUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
        if (category == null) {
            category = PostCategory.preview;
        }
        if (isPublished == null) {
            isPublished = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}