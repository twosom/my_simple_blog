package com.icloud.my_portfolio.service;

import com.icloud.my_portfolio.domain.Comment;
import com.icloud.my_portfolio.domain.CommentLike;
import com.icloud.my_portfolio.repository.CommentLikeCustomRepository;
import com.icloud.my_portfolio.repository.CommentLikeRepository;
import com.icloud.my_portfolio.repository.UserCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeCustomRepository commentLikeCustomRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final UserCustomRepository userCustomRepository;


    public void activeLike(Long commentId, String username) {
        Long userId = userCustomRepository.findIdByUsername(username);

        List<CommentLike> findCommentLike = commentLikeCustomRepository.findInactive(commentId, userId);
        if (!findCommentLike.isEmpty()) {
            CommentLike commentLike = findCommentLike.get(0);
            commentLike.active();
        } else {
            Comment comment = new Comment(commentId);
            CommentLike commentLike = new CommentLike(comment, userId, username);
            commentLike.active();
            commentLikeRepository.save(commentLike);
            commentLikeCustomRepository.updateCount();
        }
    }

    public void inactiveLike(Long commentId, String username) {
        Long userId = userCustomRepository.findIdByUsername(username);
        CommentLike findCommentLike = commentLikeCustomRepository.findActiveCommentLike(commentId, userId);

        /* Dirty Checking */
        findCommentLike.inactive();
    }
}
