package com.icloud.my_portfolio.repository;

import com.icloud.my_portfolio.domain.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

import static com.icloud.my_portfolio.domain.QPostLike.*;
import static com.icloud.my_portfolio.domain.QUser.*;

@Repository
public class PostLikeCustomRepository {

    private final JPAQueryFactory queryFactory;

    public PostLikeCustomRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    public PostLike findByPostIdAndUsername(Long postId, String username) {
        return queryFactory
                .select(postLike)
                .from(postLike)
                .join(postLike.user, user).fetchJoin()
                .where(postLike.post.id.eq(postId).and(postLike.user.username.eq(username).and(postLike.status.eq(PostLikeStatus.Y))))
                .fetchOne();
    }

    public List<PostLike> findUnActive(Long postId, String username) {
        return queryFactory
                .select(postLike)
                .from(postLike)
                .join(postLike.user, user).fetchJoin()
                .where(postLike.post.id.eq(postId).and(postLike.user.username.eq(username).and(postLike.status.eq(PostLikeStatus.N))))
                .fetch();

    }
}
