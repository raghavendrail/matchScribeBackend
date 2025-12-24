package com.matchscribe.matchscribe_backend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matchscribe.matchscribe_backend.dto.tag.TagDto;
import com.matchscribe.matchscribe_backend.entity.Post;
import com.matchscribe.matchscribe_backend.entity.PostTag;
import com.matchscribe.matchscribe_backend.entity.PostTagId;
import com.matchscribe.matchscribe_backend.entity.Tag;
import com.matchscribe.matchscribe_backend.repository.PostRepository;
import com.matchscribe.matchscribe_backend.repository.PostTagRepository;
import com.matchscribe.matchscribe_backend.service.PostTagService;
import com.matchscribe.matchscribe_backend.service.TagService;

@Service
@Transactional
public class PostTagServiceImpl implements PostTagService {

	private final PostTagRepository postTagRepository;
	private final PostRepository postRepository;
	private final TagService tagService;

	public PostTagServiceImpl(PostTagRepository postTagRepository, PostRepository postRepository,
			TagService tagService) {

		this.postTagRepository = postTagRepository;
		this.postRepository = postRepository;
		this.tagService = tagService;
	}

	@Override
	public void syncPostTags(Long postId, List<TagDto> tagInputs) {

		Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

		// 1️⃣ Remove old relations
		List<PostTag> existing = postTagRepository.findByIdPostId(postId);
		for (PostTag pt : existing) {
			tagService.decrementUsage(pt.getTag());
		}
		postTagRepository.deleteByIdPostId(postId);

		// 2️⃣ Create new relations
		List<String> tagSlugsCache = new ArrayList<>();

		for (TagDto input : tagInputs) {

			Tag tag = tagService.getOrCreate(input.getName(), input.getType());

			PostTag postTag = new PostTag();
			PostTagId id = new PostTagId(postId, tag.getSl());

			postTag.setId(id);
			postTag.setPost(post);
			postTag.setTag(tag);

			postTagRepository.save(postTag);

			tagService.incrementUsage(tag);
			tagSlugsCache.add(tag.getSlug());
		}

		// 3️⃣ Update cache array
		post.setTagSlugs(tagSlugsCache.toArray(new String[0]));
		postRepository.save(post);
	}
}
