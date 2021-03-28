package com.icloud.my_portfolio.service.post;


import com.icloud.my_portfolio.controller.dto.PostListDto;
import com.icloud.my_portfolio.domain.CommentStatus;
import com.icloud.my_portfolio.domain.Post;
import com.icloud.my_portfolio.domain.PostLikeStatus;
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
