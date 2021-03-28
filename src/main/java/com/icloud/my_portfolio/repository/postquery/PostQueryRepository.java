package com.icloud.my_portfolio.repository.postquery;

import com.icloud.my_portfolio.controller.dto.PostListDto;
import com.icloud.my_portfolio.domain.*;
import com.icloud.my_portfolio.exception.PostNotFoundException;
import com.icloud.my_portfolio.repository.postquery.dto.CommentViewDto;
import com.icloud.my_portfolio.repository.postquery.dto.PostViewDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.icloud.my_portfolio.domain.QCategory.*;
import static com.icloud.my_portfolio.domain.QComment.*;
import static com.icloud.my_portfolio.domain.QPost.*;
import static com.icloud.my_portfolio.domain.QUser.*;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final JPAQueryFactory queryFactory;

//    public Page<PostListDto> findAllByStatus(PostStatus postStatus, Pageable pageable) {
//        List<PostListDto> content = queryFactory
//                .select(new QPostListDto(
//                        post.id.as("postId"),
//                        post.title,
//                        post.content,
//                        user.username,
//                        post.createdDate,
//                        category.name.as("categoryName")))
//                .from(post)
//                .leftJoin(post.user, user)
//                .leftJoin(post.category, category)
//                .where(post.status.eq(postStatus))
//                .orderBy(post.createdDate.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        long total = queryFactory
//                .selectFrom(post)
//                .where(post.status.eq(postStatus))
//                .fetchCount();
//
//        return new PageImpl<>(content, pageable, total);
//    }

    public PostViewDto findViewPost(Long id) {
        PostViewDto result = findPost(id);
        try {
            List<CommentViewDto> comments = queryFactory
                    .select(Projections.fields(CommentViewDto.class,
                            comment.id,
                            comment.createdDate,
                            user.username,
                            post.id.as("postId"),
                            comment.content))
                    .from(comment)
                    .leftJoin(comment.post, post)
                    .leftJoin(comment.user, user)
                    .where(comment.status.eq(CommentStatus.Y).and(comment.post.id.eq(id)))
                    .orderBy(comment.createdDate.desc())
                    .fetch();
            result.setComments(comments);

            return result;
        } catch (NullPointerException e) {
            throw new PostNotFoundException("해당 게시글을 찾을 수 없습니다.");
        }

    }

    private PostViewDto findPost(Long id) {
        return queryFactory
                .select(Projections.fields(
                        PostViewDto.class,
                        post.id,
                        post.title,
                        user.username,
                        post.createdDate,
                        post.category.name.as("categoryName"),
                        post.content))
                .from(post)
                .leftJoin(post.user, user)
                .leftJoin(post.category, category)
                .where(post.id.eq(id).and(post.status.eq(PostStatus.Y)))
                .fetchOne();
    }


    public Page<Post> findAll(Pageable pageable) {
        List<Post> contents = getAllPosts(pageable);


        List<Long> postIds = getPostIds(contents);

        List<Comment> comments = queryFactory
                .select(comment)
                .from(comment)
                .innerJoin(comment.user, user).fetchJoin()
                .innerJoin(comment.post, post).fetchJoin()
                .where(comment.post.id.in(postIds))
                .fetch();

        Map<Long, List<Comment>> collect = comments.stream()
                .collect(Collectors.groupingBy(comment -> comment.getPost().getId()));


        contents.forEach(post -> post.setComments(collect.get(post.getId())));

        JPAQuery<Post> countQuery = queryFactory
                .select(post)
                .from(post);

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchCount);
    }

    private List<Long> getPostIds(List<Post> contents) {
        List<Long> postIds = contents.stream().map(Post::getId)
                .collect(Collectors.toList());
        return postIds;
    }

    private List<Post> getAllPosts(Pageable pageable) {
        return queryFactory
                .select(post)
                .from(post)
                .leftJoin(post.category, category).fetchJoin()
                .innerJoin(post.user, user).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
