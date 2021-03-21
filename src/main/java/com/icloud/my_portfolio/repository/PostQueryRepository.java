package com.icloud.my_portfolio.repository;

import com.icloud.my_portfolio.controller.dto.PostListDto;
import com.icloud.my_portfolio.controller.dto.QPostListDto;
import com.icloud.my_portfolio.domain.PostStatus;
import com.icloud.my_portfolio.domain.QCategory;
import com.icloud.my_portfolio.domain.QPost;
import com.icloud.my_portfolio.domain.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.icloud.my_portfolio.domain.QCategory.*;
import static com.icloud.my_portfolio.domain.QPost.*;
import static com.icloud.my_portfolio.domain.QUser.*;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<PostListDto> findAllByStatus(PostStatus postStatus, Pageable pageable) {
        List<PostListDto> content = queryFactory
                .select(new QPostListDto(
                        post.id.as("postId"),
                        post.title,
                        post.content,
                        user.username,
                        post.createdDate,
                        category.name.as("categoryName")))
                .from(post)
                .leftJoin(post.user, user)
                .leftJoin(post.category, category)
                .where(post.status.eq(postStatus))
                .orderBy(post.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(post)
                .where(post.status.eq(postStatus))
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

}
