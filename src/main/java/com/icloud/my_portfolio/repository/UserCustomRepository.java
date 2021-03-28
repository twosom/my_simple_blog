package com.icloud.my_portfolio.repository;

import com.icloud.my_portfolio.domain.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static com.icloud.my_portfolio.domain.QUser.*;

@Repository
public class UserCustomRepository {

    private final JPAQueryFactory queryFactory;

    public UserCustomRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    public Long findIdByUsername(String username) {
        return queryFactory
                .select(user.id)
                .from(user)
                .where(user.username.eq(username))
                .fetchOne();
    }
}
