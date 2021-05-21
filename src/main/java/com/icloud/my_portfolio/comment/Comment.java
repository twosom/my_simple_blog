package com.icloud.my_portfolio.comment;


import com.icloud.my_portfolio.account.Account;
import com.icloud.my_portfolio.like.comment.CommentLike;
import com.icloud.my_portfolio.like.comment.CommentStatus;
import com.icloud.my_portfolio.domain.SuperClass;
import com.icloud.my_portfolio.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment extends SuperClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;


    @OneToMany(mappedBy = "comment")
    private List<CommentLike> commentLikes = new ArrayList<>();


    public int commentLikeCount;

    public Comment(Long commentId) {
        this.id = commentId;
    }

    public void addCommentLikeCount() {
        this.commentLikeCount += 1;
    }

    public void removeCommentLikeCount() {
        this.commentLikeCount -= 1;
    }

    @Builder
    public Comment(String content, Post post) {
        this.content = content;
        this.post = post;
    }
}
