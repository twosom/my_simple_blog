package com.icloud.my_portfolio.controller.dto;

import com.icloud.my_portfolio.domain.Category;
import com.icloud.my_portfolio.domain.Post;
import com.icloud.my_portfolio.domain.PostStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data @NoArgsConstructor
public class PostEditDto {

    private Long id;

    @NotEmpty(message = "제목은 필수입니다.")
    private String title;

    @NotEmpty(message = "내용은 필수입니다.")
    private String content;

    @NotNull(message = "카테고리는 필수입니다.")
    private Long categoryId;

    public PostEditDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }

    public Post toEntity() {
        return Post.builder()
                .title(getTitle())
                .content(getContent())
                .status(PostStatus.Y)
                .build();
    }
}
