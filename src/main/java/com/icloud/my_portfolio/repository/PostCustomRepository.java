package com.icloud.my_portfolio.repository;

import com.icloud.my_portfolio.domain.*;
import com.icloud.my_portfolio.exception.PostNotFoundException;
import com.icloud.my_portfolio.repository.postquery.dto.CommentViewDto;
import com.icloud.my_portfolio.repository.postquery.dto.PostViewDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.icloud.my_portfolio.domain.QCategory.*;
import static com.icloud.my_portfolio.domain.QComment.*;
import static com.icloud.my_portfolio.domain.QCommentLike.*;
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

    /**
     * private Long id;
     * private String title;
     * private String username;
     * private LocalDateTime createdDate;
     * private String categoryName;
     * private String content;
     * <p>
     * private List<CommentViewDto> comments;
     * private List<String> likeUsers;
     * <p>
     * (CommentViewDto Properties)
     * private Long id;
     * private LocalDateTime createdDate;
     * private String username;
     * private Long postId;
     * private String content;
     * private List<String> likeUsers = new ArrayList<>();
     */
    public PostViewDto findOneOptimization(Long id) {
        PostViewDto findPostDto = findPostAsDto(id);

        List<String> postLikeUsernames = findPostLikeUsernames(id);
        findPostDto.setLikeUsers(postLikeUsernames);

        List<CommentViewDto> findCommentDto = findCommentAsDto(id);
        findCommentDto.forEach(comment -> comment.setPostId(id));

        List<Long> commentIds = findCommentDto.stream().map(CommentViewDto::getId)
                .collect(Collectors.toList());

        /* commentId 와 좋아요 누른 username 이 한 쌍 */
        List<Tuple> findCommentLikeUsers = queryFactory
                .select(commentLike.comment.id, commentLike.username)
                .from(commentLike)
                .where(commentLike.comment.id.in(commentIds).and(commentLike.status.eq(CommentLikeStatus.Y)))
                .fetch();


        /* CommentDto 리스트를 순회하면서 좋아요 한 유저이름들 추가 */
        for (CommentViewDto commentViewDto : findCommentDto) {
            List<String> likeUsers = new ArrayList<>();

            findCommentLikeUsers
                    .forEach(likeUser -> {
                        if (commentViewDto.getId() == likeUser.get(commentLike.comment.id)) {
                            likeUsers.add(likeUser.get(commentLike.username));
                        }
                    });
            commentViewDto.setLikeUsers(likeUsers);
        }

        findPostDto.setComments(findCommentDto);
        return findPostDto;
    }

    private List<String> findPostLikeUsernames(Long id) {
        return queryFactory
                .select(postLike.username)
                .from(postLike)
                .where(postLike.post.id.eq(id).and(postLike.status.eq(PostLikeStatus.Y)))
                .fetch();
    }

    private List<CommentViewDto> findCommentAsDto(Long id) {
        return queryFactory
                .select(Projections.fields(
                        CommentViewDto.class,
                        comment.id,
                        comment.createdDate,
                        comment.user.username,
                        comment.content))
                .from(comment)
                .innerJoin(comment.user, user)
                .where(comment.post.id.eq(id))
                .fetch();
    }

    private PostViewDto findPostAsDto(Long id) {
        return queryFactory
                .select(Projections.fields(
                        PostViewDto.class,
                        post.id,
                        post.title,
                        post.user.username,
                        post.createdDate,
                        post.category.name,
                        post.content))
                .from(post)
                .innerJoin(post.user, user)
                .leftJoin(post.category, category)
                .where(post.id.eq(id).and(post.status.eq(PostStatus.Y)))
                .fetchOne();
    }

    public Post findOne(Long id) {
        /* 컬렉션을 제외하고는 페치조인 */
        try {
            Post post = findPost(id);
            List<Comment> comments = findComment(post.getId());
            List<PostLike> postLikes = findPostLike(post.getId());

            List<Long> commentIds = comments.stream()
                    .map(Comment::getId)
                    .collect(Collectors.toList());

            List<CommentLike> commentLikes = queryFactory
                    .select(commentLike)
                    .from(commentLike)
                    .innerJoin(commentLike.comment, comment).fetchJoin()
                    .where(commentLike.comment.id.in(commentIds)
                            .and(commentLike.status.eq(CommentLikeStatus.Y)))
                    .fetch();


            Map<Long, List<CommentLike>> collect = commentLikes
                    .stream().collect(Collectors.groupingBy(commentLike -> commentLike.getComment().getId()));

            comments.forEach(comment -> comment.setCommentLikes(collect.get(comment.getId())));


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
