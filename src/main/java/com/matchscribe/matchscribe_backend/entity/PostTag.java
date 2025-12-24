package com.matchscribe.matchscribe_backend.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "post_tags")
public class PostTag {

	@EmbeddedId
	private PostTagId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("postId")
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("tagSl")
	@JoinColumn(name = "tag_sl", nullable = false)
	private Tag tag;

	// -------- getters & setters --------

	public PostTagId getId() {
		return id;
	}

	public void setId(PostTagId id) {
		this.id = id;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
}
