package com.icloud.my_portfolio.comment.service;


import com.icloud.my_portfolio.comment.repository.CommentRepository;
import com.icloud.my_portfolio.comment.Comment;
import com.icloud.my_portfolio.like.comment.CommentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment createComment(Comment comment) {
        comment.setCreatedDate(LocalDateTime.now());
        comment.setStatus(CommentStatus.Y);
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(RuntimeException::new);
        findComment.setStatus(CommentStatus.N);
    }
}
