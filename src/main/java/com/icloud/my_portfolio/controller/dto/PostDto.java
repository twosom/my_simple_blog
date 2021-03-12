package com.icloud.my_portfolio.controller.dto;


import com.icloud.my_portfolio.domain.Comment;
import com.icloud.my_portfolio.domain.Post;
import com.icloud.my_portfolio.domain.PostStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor
public class PostDto {

    private Long id;

    private Long userId;

    @NotNull(message = "카테고리는 필수입니다.")
    private Long categoryId;

    @NotEmpty(message = "제목은 필수입니다.")
    private String title;
    private String username;

    @NotEmpty(message = "내용은 필수입니다.")
    private String content;
    private LocalDateTime createdDate;
    private String categoryName;

    private List<CommentDto> comments = new ArrayList<>();

    public PostDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdDate = post.getCreatedDate();
        this.username = post.getUser().getUsername();
        if (post.getCategory() == null) {
            this.categoryName = "";
        } else {
            this.categoryName = post.getCategory().getName();
        }

        this.comments = post.getComments()
                .stream().map(comment -> {
                    CommentDto commentDto = new CommentDto(comment);
                    return commentDto;
                }).collect(Collectors.toList());
    }

    //==삭제된 댓글과 함께 조회되는 것을 막기 위한 DTO생성자==//
    public PostDto(Post post, List<Comment> enabledComments) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdDate = post.getCreatedDate();
        this.username = post.getUser().getUsername();
        if (post.getCategory() == null) {
            this.categoryName = "";
        } else {
            this.categoryName = post.getCategory().getName();
        }

        this.comments = enabledComments
                .stream().map(comment -> {
                    CommentDto commentDto = new CommentDto(comment);
                    return commentDto;
                }).collect(Collectors.toList());
    }

    public Post toEntity() {
        return Post.builder()
                .title(getTitle())
                .content(getContent())
                .status(PostStatus.Y)
                .build();
    }
}
