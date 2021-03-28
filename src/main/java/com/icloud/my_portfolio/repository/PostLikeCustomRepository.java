package com.icloud.my_portfolio.repository;

import com.icloud.my_portfolio.domain.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

import static com.icloud.my_portfolio.domain.QPost.*;
import static com.icloud.my_portfolio.domain.QPostLike.*;
import static com.icloud.my_portfolio.domain.QUser.*;

@Repository
public class PostLikeCustomRepository {

    private final JPAQueryFactory queryFactory;

    public PostLikeCustomRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    public PostLike findActivePostLike(Long postId, Long userId) {
        return queryFactory
                .select(postLike)
                .from(postLike)
                .innerJoin(postLike.post, post).fetchJoin()
                .where(postLike.post.id.eq(postId).and(postLike.userId.eq(userId).and(postLike.status.eq(PostLikeStatus.Y))))
                .fetchOne();
    }


    public List<PostLike> findInacative(Long postId, Long userId) {
        return queryFactory
                .select(postLike)
                .from(postLike)
                .join(postLike.post, post).fetchJoin()
                .where(postLike.post.id.eq(postId)
                        .and(postLike.userId.eq(userId)
                                .and(postLike.status.eq(PostLikeStatus.N))))
                .fetch();
    }

    public void updateCount() {
        queryFactory
                .update(post)
                .set(post.postLikeCount, 1)
                .execute();
    }
}
