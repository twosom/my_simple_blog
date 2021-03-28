package com.icloud.my_portfolio.service;

import com.icloud.my_portfolio.domain.Post;
import com.icloud.my_portfolio.domain.PostLike;
import com.icloud.my_portfolio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserCustomRepository userCustomRepository;
    private final PostLikeCustomRepository postLikeCustomRepository;

    public void activeLike(Long postId, String username) {

        Long userId = userCustomRepository.findIdByUsername(username);

        List<PostLike> findPostLike = postLikeCustomRepository.findInacative(postId, userId);
        if (!findPostLike.isEmpty()) {
            PostLike postLike = findPostLike.get(0);
            postLike.active();
        } else {
            Post post = new Post(postId);
            PostLike postLike = new PostLike(post, userId, username);
            postLike.active();
            postLikeRepository.save(postLike);
            postLikeCustomRepository.updateCount();
        }
    }

    public void inactiveLike(Long postId, String username) {
        Long userId = userCustomRepository.findIdByUsername(username);
        PostLike findPostLike = postLikeCustomRepository.findActivePostLike(postId, userId);

        /* Dirty Checking */
        findPostLike.inactive();
    }
}
