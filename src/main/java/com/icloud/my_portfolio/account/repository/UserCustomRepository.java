package com.icloud.my_portfolio.account.repository;

import com.icloud.my_portfolio.account.QAccount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import static com.icloud.my_portfolio.account.QAccount.*;


@Repository
public class UserCustomRepository {

    private final JPAQueryFactory queryFactory;

    public UserCustomRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    public Long findIdByUsername(String username) {
        return queryFactory
                .select(account.id)
                .from(account)
                .where(account.username.eq(username))
                .fetchOne();
    }
}
