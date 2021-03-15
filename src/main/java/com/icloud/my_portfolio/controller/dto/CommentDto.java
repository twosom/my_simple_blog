package com.icloud.my_portfolio.controller.dto;

import com.icloud.my_portfolio.domain.Comment;
import com.icloud.my_portfolio.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class CommentDto {
    private Long id;
    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;
    private Long postId;
    private Long userId;
    private String username;
    private LocalDateTime createdDate;


    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.postId = comment.getPost().getId();
        this.createdDate = comment.getCreatedDate();
        this.username = comment.getUser().getUsername();
    }

    public Comment toEntity() {
        return Comment.builder()
                .content(getContent())
                .post(new Post(getPostId()))
                .build();
    }
}
