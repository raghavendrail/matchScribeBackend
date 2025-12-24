package com.matchscribe.matchscribe_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matchscribe.matchscribe_backend.entity.PostTag;
import com.matchscribe.matchscribe_backend.entity.PostTagId;

public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {

	List<PostTag> findByIdPostId(Long postId);

	void deleteByIdPostId(Long postId);
}