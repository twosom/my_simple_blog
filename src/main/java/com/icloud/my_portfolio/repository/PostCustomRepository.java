package com.icloud.my_portfolio.repository;

import com.icloud.my_portfolio.domain.*;
import com.icloud.my_portfolio.exception.PostNotFoundException;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.validation.constraints.Null;
import java.util.List;

import static com.icloud.my_portfolio.domain.QCategory.*;
import static com.icloud.my_portfolio.domain.QComment.*;
import static com.icloud.my_portfolio.domain.QPost.*;
import static com.icloud.my_portfolio.domain.QUser.*;

@Repository
public class PostCustomRepository {

    private final JPAQueryFactory queryFactory;

    public PostCustomRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    public Page<Post> findAllPosts(Pageable pageable) {
        List<Post> content = queryFactory
                .select(post)
                .from(post)
                .innerJoin(post.category, category).fetchJoin()
                .innerJoin(post.user, user).fetchJoin()
                .where(post.status.eq(PostStatus.Y))
                .orderBy(post.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Post> countQuery = queryFactory
                .select(post)
                .from(post)
                .where(post.status.eq(PostStatus.Y));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    public Post findOne(Long id) {
        /* 컬렉션을 제외하고는 페치조인 */
        try {
            Post post = findPost(id);
            List<Comment> comments = findComment(post.getId());

            post.setComments(comments);

            return post;
        } catch (NullPointerException e) {
            throw new PostNotFoundException();
        }

    }

    private List<Comment> findComment(Long id) {
        return queryFactory
                .select(comment)
                .from(comment)
                .where(comment.post.id.eq(id).and(comment.status.eq(CommentStatus.Y)))
                .orderBy(comment.createdDate.desc())
                .fetch();
    }

    private Post findPost(Long id) {
        return queryFactory
                .select(post)
                .from(post)
                .innerJoin(post.user, user).fetchJoin()
                .innerJoin(post.category, category).fetchJoin()
                .where(post.id.eq(id).and(post.status.eq(PostStatus.Y)))
                .fetchOne();
    }
}
