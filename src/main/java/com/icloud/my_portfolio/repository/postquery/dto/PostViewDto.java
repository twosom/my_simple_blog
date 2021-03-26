package com.icloud.my_portfolio.repository.postquery.dto;

import com.icloud.my_portfolio.domain.Post;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data @NoArgsConstructor
public class PostViewDto {

    private Long id;
    private String title;
    private String username;
    private LocalDateTime createdDate;
    private String categoryName;
    private String content;

    private List<CommentViewDto> comments;

    public PostViewDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUser().getUsername();
        this.createdDate = post.getCreatedDate();
        this.categoryName = post.getCategory().getName();
        this.content = post.getContent();

        this.comments = post.getComments()
                .stream().map(comment ->
                        new CommentViewDto(
                                comment.getId(),
                                comment.getCreatedDate(),
                                this.username, this.id,
                                comment.getContent()))
                .collect(Collectors.toList());
    }
}
