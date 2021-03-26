package com.icloud.my_portfolio.service;


import com.icloud.my_portfolio.controller.dto.PostListDto;
import com.icloud.my_portfolio.domain.CommentStatus;
import com.icloud.my_portfolio.domain.Post;
import com.icloud.my_portfolio.domain.PostStatus;
import com.icloud.my_portfolio.exception.PostNotFoundException;
import com.icloud.my_portfolio.repository.PostCustomRepository;
import com.icloud.my_portfolio.repository.PostRepository;
import com.icloud.my_portfolio.repository.PostRepositoryWithJpql;
import com.icloud.my_portfolio.repository.postquery.dto.PostViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    //==페이징 처리가 필요한 비즈니스로직 구현을 위한 Repository==//
    private final PostRepository postRepository;
    private final PostRepositoryWithJpql postRepositoryWithJpql;

    private final PostCustomRepository postCustomRepository;



    public Page<PostListDto> findPosts(Pageable pageable) {
        Page<Post> allPosts = postCustomRepository.findAllPosts(pageable);

        List<PostListDto> content = allPosts.getContent()
                .stream().map(post -> new PostListDto(post))
                .collect(Collectors.toList());


        long total = allPosts.getTotalElements();

        return new PageImpl(content, pageable, total);
    }


    //== 게시글 조회 ==//
    public PostViewDto findOne(Long id) {
        Post findPost = postCustomRepository.findOne(id);

        return new PostViewDto(findPost);
    }


    public Post createPost(Post post) {
        post.setCreatedDate(LocalDateTime.now());
        return postRepositoryWithJpql.save(post);
    }

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
    public void deletePost(Long id) {
        try {
            Post oldPost = postRepositoryWithJpql.findByIdAndStatus(id, PostStatus.Y);
            oldPost.setStatus(PostStatus.N);
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


    //==게시글 id와 게시글 상태, 그리고 댓글 상태로 조회==//
    public Post findByIdAndStatus(Long id, PostStatus postStatus, CommentStatus commentStatus) {
        try {
            Post post = postRepositoryWithJpql.findByIdAndStatus(id, postStatus, commentStatus);
            return post;
        } catch (PostNotFoundException e) {
            throw e;
        }
    }


}
