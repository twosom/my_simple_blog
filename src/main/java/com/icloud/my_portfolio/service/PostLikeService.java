package com.icloud.my_portfolio.service;

import com.icloud.my_portfolio.domain.Post;
import com.icloud.my_portfolio.domain.PostLike;
import com.icloud.my_portfolio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserCustomRepository userCustomRepository;
    private final PostLikeCustomRepository postLikeCustomRepository;

    public LinkedHashMap<String, Object> postLikeEvent(Long postId, String username) {

        Long userId = userCustomRepository.findIdByUsername(username);
        PostLike findPostLike = postLikeCustomRepository.findLike(postId, userId);
        try {
            switch (findPostLike.getStatus()) {
                case Y:
                    // 이미 좋아요가 눌린 경우 - 이 경우에는 다시 N으로 만들어준다.
                    findPostLike.inactive();
                    break;
                case N:
                    // 좋아요를 눌렀다가 취소한 경우
                    findPostLike.active();
                    break;
            }
        } catch (NullPointerException e) {
            // findPostLike 객체가 없는 경우(아예 처음 좋아요를 누르는 경우)
            Post post = new Post(postId);
            findPostLike = new PostLike(post, userId, username);
            findPostLike.active();
            postLikeRepository.save(findPostLike);
            postLikeCustomRepository.updateCount(postId);
        }

        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("status", findPostLike.getStatus());
        result.put("count", postLikeCustomRepository.getCount(postId));

        return result;
    }

    public void inactiveLike(Long postId, String username) {
        Long userId = userCustomRepository.findIdByUsername(username);
        PostLike findPostLike = postLikeCustomRepository.findActivePostLike(postId, userId);

        /* Dirty Checking */
        findPostLike.inactive();
    }
}
