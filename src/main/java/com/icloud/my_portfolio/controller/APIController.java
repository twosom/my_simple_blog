package com.icloud.my_portfolio.controller;

import com.icloud.my_portfolio.controller.dto.PostListDto;
import com.icloud.my_portfolio.domain.CommentStatus;
import com.icloud.my_portfolio.domain.Post;
import com.icloud.my_portfolio.domain.PostStatus;
import com.icloud.my_portfolio.repository.postquery.PostQueryRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class APIController {

    private final PostQueryRepository postQueryRepository;

    @GetMapping("/api/posts")
    public Page<PostQueryDto>allPosts(Pageable pageable) {
        Page<Post> result = postQueryRepository.findAll(pageable);

        List<Post> posts = result.getContent();
        List<PostQueryDto> content = posts.stream().map(PostQueryDto::new)
                .collect(Collectors.toList());

        long total = result.getTotalElements();

        return new PageImpl(content, pageable, total);
    }

    @Data
    static class PostQueryDto {
        private Long postId;
        private String title;
        private String categoryName;
        private String content;
        private PostStatus postStatus;
        private String username;

        private List<CommentQueryDto> comments;

        public PostQueryDto(Post post) {
            this.postId = post.getId();
            this.title = post.getTitle();
            if (post.getCategory() != null) {
                this.categoryName = post.getCategory().getName();
            }

            this.content = post.getContent();
            this.postStatus = post.getStatus();
            this.username = post.getUser().getUsername();

            if (post.getComments() != null) {
                this.comments = post.getComments()
                        .stream().map(comment -> new CommentQueryDto(comment.getId(), comment.getContent(), comment.getUser().getUsername(), comment.getStatus()))
                        .collect(Collectors.toList());
            }
        }
    }

    @Data
    @NoArgsConstructor
    static class CommentQueryDto {

        private Long commentId;
        private String content;
        private String username;
        private CommentStatus commentStatus;

        public CommentQueryDto(Long commentId, String content, String username, CommentStatus commentStatus) {
            this.commentId = commentId;
            this.content = content;
            this.username = username;
            this.commentStatus = commentStatus;
        }
    }



}
