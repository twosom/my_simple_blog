package com.icloud.my_portfolio.like.comment.repository;

import com.icloud.my_portfolio.like.comment.CommentLike;
import com.icloud.my_portfolio.like.comment.CommentLikeStatus;
import com.icloud.my_portfolio.like.comment.CommentStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static com.icloud.my_portfolio.comment.QComment.comment;
import static com.icloud.my_portfolio.like.comment.QCommentLike.commentLike;


@Repository
public class CommentLikeCustomRepository {

    private final JPAQueryFactory queryFactory;

    public CommentLikeCustomRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    public CommentLike findCommentLike(Long commentId, Long userId) {
        return queryFactory
                .select(commentLike)
                .from(commentLike)
                .where(commentLike.comment.id.eq(commentId)
                        .and(commentLike.userId.eq(userId)))
                .fetchOne();
    }

    public void updateCount(Long commentId) {
        queryFactory
                .update(comment)
                .set(comment.commentLikeCount, comment.commentLikeCount.add(1))
                .where(comment.id.eq(commentId))
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

    public Integer getCount(Long commentId) {
        return queryFactory
                .select(comment.commentLikeCount)
                .from(comment)
                .where(comment.id.eq(commentId))
                .fetchOne();
    }

    public CommentStatus getCommentStatusById(Long commentId) {
        return queryFactory
                .select(comment.status)
                .from(comment)
                .where(comment.id.eq(commentId))
                .fetchOne();
    }
}
