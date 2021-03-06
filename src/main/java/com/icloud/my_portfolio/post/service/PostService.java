package com.icloud.my_portfolio.post.service;


import com.icloud.my_portfolio.post.Post;
import com.icloud.my_portfolio.like.post.PostLikeStatus;
import com.icloud.my_portfolio.post.PostStatus;
import com.icloud.my_portfolio.exception.PostNotFoundException;
import com.icloud.my_portfolio.post.repository.PostRepository;
import com.icloud.my_portfolio.post.repository.PostRepositoryWithJpql;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    //==페이징 처리가 필요한 비즈니스로직 구현을 위한 Repository==//
    private final PostRepository postRepository;
    private final PostRepositoryWithJpql postRepositoryWithJpql;


    @Transactional
    public Post createPost(Post post) {
        post.setCreatedDate(LocalDateTime.now());
        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(Long id, Post post) {
        try {
            Post oldPost = postRepositoryWithJpql.findByIdAndStatus(id, PostStatus.Y);
            oldPost.setContent(post.getContent());
            oldPost.setCategory(post.getCategory());
            oldPost.setTitle(post.getTitle());
            return oldPost;
        } catch (PostNotFoundException e) {
            throw e;
        }
    }

    //==데이터베이스에서 바로 삭제하지 않고 상태만 변경==//
    @Transactional
    public void deletePost(Long id) {
        try {
            Post oldPost = postRepositoryWithJpql.findByIdAndStatus(id, PostStatus.Y);
            oldPost.setStatus(PostStatus.N);
            oldPost.getPostLikes()
                    .forEach(postLike -> postLike.setStatus(PostLikeStatus.N));
        } catch (PostNotFoundException e) {
            throw e;
        }
    }

    //==게시글 id와 상태로 조회==//
    public Post findByIdAndStatus(Long id, PostStatus status) {
        try {
            Post post = postRepositoryWithJpql.findByIdAndStatus(id, status);
            return post;
        } catch (PostNotFoundException e) {
            throw e;
        }
    }

}
