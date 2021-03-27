package com.icloud.my_portfolio.controller.dto;

import com.icloud.my_portfolio.domain.Post;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostListDto {

    private Long postId;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdDate;
    private String categoryName;

    private int likeCount = 0;

    @QueryProjection
    public PostListDto(Long postId, String title, String content, String username, LocalDateTime createdDate, String categoryName) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.username = username;
        this.createdDate = createdDate;
        this.categoryName = categoryName;
    }

    public PostListDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getUser().getUsername();
        this.createdDate = post.getCreatedDate();
        if (post.getCategory() != null) {
            this.categoryName = post.getCategory().getName();
        }
        if (post.getPostLikes() != null) {
            this.likeCount = post.getPostLikes().size();
        }
    }
}
