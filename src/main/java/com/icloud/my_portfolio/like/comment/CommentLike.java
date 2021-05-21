package com.icloud.my_portfolio.like.comment;

import com.icloud.my_portfolio.comment.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor
@ToString(of = {"id", "userId", "username", "status"})
public class CommentLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    //=누가 저장했는지 저장할 컬럼=//
    private Long userId;
    private String username;

    @Enumerated(EnumType.STRING)
    private CommentLikeStatus status;

    public CommentLike(Comment comment, Long userId, String username) {
        this.comment = comment;
        this.userId = userId;
        this.username = username;
    }

    public void active() {
        this.status = CommentLikeStatus.Y;
        this.comment.addCommentLikeCount();
    }

    public void inactive() {
        this.status = CommentLikeStatus.N;
        this.comment.removeCommentLikeCount();
    }
}
