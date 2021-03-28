package com.icloud.my_portfolio.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter @Setter
public class PostLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private PostLikeStatus status;


    public void active() {
        this.status = PostLikeStatus.Y;
    }

    public void inactive() {
        this.status = PostLikeStatus.N;
    }


    public void addPost(Post post) {
        this.post = post;
    }

    public void addUser(User user) {
        this.user = user;
//        user.addPostLike(this);
    }
}
