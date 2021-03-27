package com.icloud.my_portfolio.service;

import com.icloud.my_portfolio.domain.Post;
import com.icloud.my_portfolio.domain.PostLike;
import com.icloud.my_portfolio.domain.PostLikeStatus;
import com.icloud.my_portfolio.domain.User;
import com.icloud.my_portfolio.exception.PostNotFoundException;
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
    private final UserRepository userRepository;
    private final PostLikeCustomRepository postLikeCustomRepository;

    public void activeLike(Long postId, String username) {

        List<PostLike> findPostLike = postLikeCustomRepository.findUnActive(postId, username);
        if (!findPostLike.isEmpty()) {
            PostLike postLike = findPostLike.get(0);
            postLike.active();
        } else {
            Post post = new Post(postId);
            User findUser = userRepository.findByUsername(username).get(0);

            PostLike postLike = new PostLike();
            postLike.addPost(post);
            postLike.addUser(findUser);
            postLike.active();

            postLikeRepository.save(postLike);
        }
    }

    public void inactiveLike(Long postId, String username) {
        PostLike findPostLike = postLikeCustomRepository.findByPostIdAndUsername(postId, username);

        /* Dirty Checking */
        findPostLike.inactive();
    }
}
