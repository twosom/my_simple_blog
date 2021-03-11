package com.icloud.my_portfolio.service;


import com.icloud.my_portfolio.domain.Comment;
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
        return commentRepositoryWithJpql.save(comment);
    }

    public void deleteComment(Long commentId) {
        commentRepositoryWithJpql.delete(commentId);
    }
}
