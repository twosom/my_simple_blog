package com.icloud.my_portfolio.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CommentViewDto {

    private Long id;
    private LocalDateTime createdDate;
    private String username;
    private Long postId;
    private String content;
    private List<String> likeUsers = new ArrayList<>();

    public CommentViewDto(Long id, LocalDateTime createdDate, String username, Long postId, String content) {
        this.id = id;
        this.createdDate = createdDate;
        this.username = username;
        this.postId = postId;
        this.content = content;
    }
}
