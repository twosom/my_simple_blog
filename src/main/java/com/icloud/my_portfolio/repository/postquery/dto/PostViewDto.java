package com.icloud.my_portfolio.repository.postquery.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostViewDto {

    private Long id;
    private String title;
    private String username;
    private LocalDateTime createdDate;
    private String categoryName;
    private String content;

    private List<CommentViewDto> comments;


}
