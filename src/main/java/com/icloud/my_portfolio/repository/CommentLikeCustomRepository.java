package com.icloud.my_portfolio.repository;

import com.icloud.my_portfolio.domain.CommentLike;
import com.icloud.my_portfolio.domain.CommentLikeStatus;
import com.icloud.my_portfolio.domain.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

import static com.icloud.my_portfolio.domain.QComment.*;
import static com.icloud.my_portfolio.domain.QCommentLike.*;

@Repository
public class CommentLikeCustomRepository {

    private final JPAQueryFactory queryFactory;

    public CommentLikeCustomRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    public List<CommentLike> findInactive(Long commentId, Long userId) {
        return queryFactory
                .select(commentLike)
                .from(commentLike)
                .where(commentLike.comment.id.eq(commentId)
                        .and(commentLike.userId.eq(userId)
                                .and(commentLike.status.eq(CommentLikeStatus.N))))
                .fetch();

    }

    public void updateCount() {
        queryFactory
                .update(comment)
                .set(comment.commentLikeCount, 1)
                .execute();
    }

    public CommentLike findActiveCommentLike(Long commentId, Long userId) {
        return queryFactory
                .select(commentLike)
                .from(commentLike)
                .where(commentLike.comment.id.eq(commentId).
                        and(commentLike.userId.eq(userId).
                                and(commentLike.status.eq(CommentLikeStatus.Y))))
                .fetchOne();
    }
}
