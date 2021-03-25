package com.icloud.my_portfolio.repository.postquery;

import com.icloud.my_portfolio.controller.dto.PostListDto;
import com.icloud.my_portfolio.controller.dto.QPostListDto;
import com.icloud.my_portfolio.domain.CommentStatus;
import com.icloud.my_portfolio.domain.PostStatus;
import com.icloud.my_portfolio.domain.QComment;
import com.icloud.my_portfolio.exception.PostNotFoundException;
import com.icloud.my_portfolio.repository.postquery.dto.CommentViewDto;
import com.icloud.my_portfolio.repository.postquery.dto.PostViewDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.icloud.my_portfolio.domain.QCategory.*;
import static com.icloud.my_portfolio.domain.QComment.*;
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


}
