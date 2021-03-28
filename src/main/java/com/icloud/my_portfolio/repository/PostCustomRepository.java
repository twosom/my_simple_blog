package com.icloud.my_portfolio.repository;

import com.icloud.my_portfolio.domain.*;
import com.icloud.my_portfolio.exception.PostNotFoundException;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.icloud.my_portfolio.domain.QCategory.*;
import static com.icloud.my_portfolio.domain.QComment.*;
import static com.icloud.my_portfolio.domain.QPost.*;
import static com.icloud.my_portfolio.domain.QPostLike.*;
import static com.icloud.my_portfolio.domain.QUser.*;

@Repository
public class PostCustomRepository {

    private final JPAQueryFactory queryFactory;

    public PostCustomRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    public Page<Post> findAllPosts(Pageable pageable) {
        List<Post> content = getPosts(pageable);

        JPAQuery<Post> countQuery = queryFactory
                .select(post)
                .from(post)
                .where(post.status.eq(PostStatus.Y));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    private List<Post> getPosts(Pageable pageable) {
        List<Post> content = queryFactory
                .select(post)
                .from(post)
                .leftJoin(post.category, category).fetchJoin()
                .innerJoin(post.user, user).fetchJoin()
                .where(post.status.eq(PostStatus.Y))
                .orderBy(post.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return content;
    }

    public Post findOne(Long id) {
        /* 컬렉션을 제외하고는 페치조인 */
        try {
            Post post = findPost(id);
            List<Comment> comments = findComment(post.getId());
            List<PostLike> postLikes = findPostLike(post.getId());

            post.setComments(comments);
            post.setPostLikes(postLikes);

            return post;
        } catch (NullPointerException e) {
            throw new PostNotFoundException();
        }

    }

    private List<PostLike> findPostLike(Long id) {
        return queryFactory
                .select(postLike)
                .from(postLike)
                .innerJoin(postLike.post, post).fetchJoin()
                .where(postLike.post.id.eq(id).and(postLike.status.eq(PostLikeStatus.Y)))
                .fetch();
    }

    private List<Comment> findComment(Long id) {
        return queryFactory
                .select(comment)
                .from(comment)
                .innerJoin(comment.user, user).fetchJoin()
                .where(comment.post.id.eq(id).and(comment.status.eq(CommentStatus.Y)))
                .orderBy(comment.createdDate.desc())
                .fetch();
    }


    private Post findPost(Long id) {
        return queryFactory
                .select(post)
                .from(post)
                .innerJoin(post.user, user).fetchJoin()
                .leftJoin(post.category, category).fetchJoin()
                .where(post.id.eq(id).and(post.status.eq(PostStatus.Y)))
                .fetchOne();
    }

    public Post findByIdAndUsername(Long id, String username) {
        try {
            return queryFactory
                    .selectFrom(post)
                    .where(post.id.eq(id).and(post.user.username.eq(username)).and(post.status.eq(PostStatus.Y)))
                    .leftJoin(post.user, user).fetchJoin()
                    .fetchOne();
        } catch (Exception e) {
            throw new PostNotFoundException("해당 게시글을 찾을 수 없습니다. id = " + id);
        }


    }
}
