package com.matchscribe.matchscribe_backend.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PostTagId implements Serializable {

	@Column(name = "post_id")
	private Long postId;

	@Column(name = "tag_sl")
	private Long tagSl;

	// -------- getters & setters --------

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public Long getTagSl() {
		return tagSl;
	}

	public void setTagSl(Long tagSl) {
		this.tagSl = tagSl;
	}

	public PostTagId(Long postId, Long tagSl) {
		this.postId = postId;
		this.tagSl = tagSl;
	}
}
