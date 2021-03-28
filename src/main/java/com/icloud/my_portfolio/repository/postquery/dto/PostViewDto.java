package com.icloud.my_portfolio.repository.postquery.dto;

import com.icloud.my_portfolio.domain.Post;
import com.icloud.my_portfolio.domain.PostLike;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class PostViewDto {

    private Long id;
    private String title;
    private String username;
    private LocalDateTime createdDate;
    private String categoryName;
    private String content;

    private List<CommentViewDto> comments;
//    private List<PostLikeViewDto> postLikes;

    private List<String> likeUsers;

    public PostViewDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUser().getUsername();
        this.createdDate = post.getCreatedDate();
        if (post.getCategory() != null) {
            this.categoryName = post.getCategory().getName();
        }
        this.content = post.getContent();

        this.comments = post.getComments()
                .stream().map(comment ->
                        new CommentViewDto(
                                comment.getId(),
                                comment.getCreatedDate(),
                                comment.getUser().getUsername(), post.getId(),
                                comment.getContent()))
                .collect(Collectors.toList());

//
//        this.postLikes = post.getPostLikes()
//                .stream().map(postLike ->
//                        new PostLikeViewDto(
//                                postLike.getId(),
//                                post.getId(),
//                                postLike.getUser().getUsername()))
//                .collect(Collectors.toList());

        this.likeUsers = post.getPostLikes()
                .stream().map(PostLike::getUsername)
                .collect(Collectors.toList());


    }
}
