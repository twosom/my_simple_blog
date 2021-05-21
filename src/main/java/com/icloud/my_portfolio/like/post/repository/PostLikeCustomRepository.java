package com.icloud.my_portfolio.like.post.repository;

import com.icloud.my_portfolio.like.post.PostLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static com.icloud.my_portfolio.like.post.QPostLike.postLike;
import static com.icloud.my_portfolio.post.QPost.post;


@Repository
public class PostLikeCustomRepository {

    private final JPAQueryFactory queryFactory;

    public PostLikeCustomRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public void updateCount(Long postId) {
        queryFactory
                .update(post)
                .set(post.postLikeCount, post.postLikeCount.add(1))
                .where(post.id.eq(postId))
                .execute();
    }

    public Integer getCount(Long postId) {
        return queryFactory
                .select(post.postLikeCount)
                .from(post)
                .where(post.id.eq(postId))
                .fetchOne();
    }

    public PostLike findLike(Long postId, Long userId) {
        return queryFactory
                .select(postLike)
                .from(postLike)
                .innerJoin(postLike.post, post).fetchJoin()
                .where(postLike.post.id.eq(postId)
                        .and(postLike.userId.eq(userId)))
                .fetchOne();
    }
}
