package com.icloud.my_portfolio.controller.dto;

import com.icloud.my_portfolio.domain.Category;
import com.icloud.my_portfolio.domain.Post;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostEditDto {

    private Long id;
    private String title;
    private String content;

    private List<PostEditCategory> categories;

    public PostEditDto(Post post, List<Category> categories) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();

        if (!categories.isEmpty()) {
            this.categories = categories.stream().map(PostEditCategory::new)
                    .collect(Collectors.toList());
        }
    }
}
