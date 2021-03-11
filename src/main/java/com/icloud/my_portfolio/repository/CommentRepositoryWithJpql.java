package com.icloud.my_portfolio.repository;


import com.icloud.my_portfolio.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryWithJpql {

    private final EntityManager em;

    public Comment save(Comment comment) {
        em.persist(comment);
        return comment;
    }

    public Comment findOne(Long id) {
        return em.find(Comment.class, id);
    }
    public void delete(Long commentId) {
        Comment findComment = findOne(commentId);
        em.remove(findComment);
    }
}
