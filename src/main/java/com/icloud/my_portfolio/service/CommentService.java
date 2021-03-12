package com.icloud.my_portfolio.service;


import com.icloud.my_portfolio.domain.Comment;
import com.icloud.my_portfolio.domain.CommentStatus;
import com.icloud.my_portfolio.repository.CommentRepositoryWithJpql;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepositoryWithJpql commentRepositoryWithJpql;

    public Comment createComment(Comment comment) {
        comment.setCreatedDate(LocalDateTime.now());
        comment.setStatus(CommentStatus.Y);
        return commentRepositoryWithJpql.save(comment);
    }

    public void deleteComment(Long commentId) {
        Comment findComment = commentRepositoryWithJpql.findOne(commentId);
        findComment.setStatus(CommentStatus.N);
    }
}
