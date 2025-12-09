package com.matchscribe.matchscribe_backend.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "post_tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostTag {

    @EmbeddedId
    private PostTagId id;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostTagId implements Serializable {
        @Column(name = "post_id")
        private Long postId;

        @Column(name = "tag_id")
        private Long tagId;
    }
}