package com.icloud.my_portfolio.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter @Setter @NoArgsConstructor
public class PostLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    //=누가 저장했는지 저장할 컬럼=//
    private Long userId;
    private String username;

    @Enumerated(EnumType.STRING)
    private PostLikeStatus status;


    public void active() {
        this.status = PostLikeStatus.Y;
        this.post.addPostLikeCount();
    }

    public void inactive() {
        this.status = PostLikeStatus.N;
        this.post.removePostLikeCount();
    }


    public void addPost(Post post) {
        this.post = post;
    }

    public PostLike(Post post, Long userId, String username) {
        this.post = post;
        this.userId = userId;
        this.username = username;
        this.status = PostLikeStatus.Y;
    }
}
