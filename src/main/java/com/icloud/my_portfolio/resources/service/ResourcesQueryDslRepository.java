package com.icloud.my_portfolio.resources.service;

import com.icloud.my_portfolio.resources.QResources;
import com.icloud.my_portfolio.resources.ResourceType;
import com.icloud.my_portfolio.resources.Resources;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.icloud.my_portfolio.resources.QResources.*;

@Repository
public class ResourcesQueryDslRepository {

    private JPAQueryFactory queryFactory;

    public ResourcesQueryDslRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    public List<Resources> getAllResources() {
        return queryFactory
                .selectFrom(resources)
                .join(resources.roleList).fetchJoin()
                .where(resources.resourceType.eq(ResourceType.URL))
                .orderBy(resources.orderNum.desc())
                .fetch();
    }
}
