package com.icloud.my_portfolio.controller.dto;

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

    @QueryProjection
    public PostListDto(Long postId, String title, String content, String username, LocalDateTime createdDate, String categoryName) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.username = username;
        this.createdDate = createdDate;
        this.categoryName = categoryName;
    }
}
