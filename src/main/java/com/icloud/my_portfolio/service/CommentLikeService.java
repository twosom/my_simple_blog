package com.icloud.my_portfolio.service;

import com.icloud.my_portfolio.domain.Comment;
import com.icloud.my_portfolio.domain.CommentLike;
import com.icloud.my_portfolio.repository.CommentLikeCustomRepository;
import com.icloud.my_portfolio.repository.CommentLikeRepository;
import com.icloud.my_portfolio.repository.UserCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeCustomRepository commentLikeCustomRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final UserCustomRepository userCustomRepository;


    public LinkedHashMap<String, Object> commentLikeEvent(Long commentId, String username) {
        Long userId = userCustomRepository.findIdByUsername(username);

//        List<CommentLike> findCommentLike = commentLikeCustomRepository.findInactive(commentId, userId);
//        if (!findCommentLike.isEmpty()) {
//            CommentLike commentLike = findCommentLike.get(0);
//            commentLike.active();
//        } else {
//            Comment comment = new Comment(commentId);
//            CommentLike commentLike = new CommentLike(comment, userId, username);
//            commentLike.active();
//            commentLikeRepository.save(commentLike);
//            commentLikeCustomRepository.updateCount(commentId);
//        }


        CommentLike findCommentLike = commentLikeCustomRepository.findCommentLike(commentId, userId);
        try {
            switch (findCommentLike.getStatus()) {
                case Y:
                    // 이미 좋아요가 눌린 경우
                    findCommentLike.inactive();
                    break;
                case N:
                    // 좋아요를 눌렀다가 취소한 경우
                    findCommentLike.active();
                    break;
            }
        } catch (NullPointerException e) {
            Comment comment = new Comment(commentId);
            findCommentLike = new CommentLike(comment, userId, username);
            findCommentLike.active();
            commentLikeRepository.save(findCommentLike);
            commentLikeCustomRepository.updateCount(commentId);
        }

        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("status", findCommentLike.getStatus());
        result.put("count", commentLikeCustomRepository.getCount(commentId));

        return result;
    }

    public void inactiveLike(Long commentId, String username) {
        Long userId = userCustomRepository.findIdByUsername(username);
        CommentLike findCommentLike = commentLikeCustomRepository.findActiveCommentLike(commentId, userId);

        /* Dirty Checking */
        findCommentLike.inactive();
    }
}
