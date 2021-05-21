package com.icloud.my_portfolio.post;


import com.icloud.my_portfolio.account.Account;
import com.icloud.my_portfolio.category.Category;
import com.icloud.my_portfolio.comment.Comment;
import com.icloud.my_portfolio.domain.*;
import com.icloud.my_portfolio.like.post.PostLike;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"id", "content", "title", "status"})
public class Post extends SuperClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @NotNull
    @Lob
    private String content;

    @NotNull
    private String title;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Account account;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PostLike> postLikes = new ArrayList<>();

    private int postLikeCount;

    public void addPostLikeCount() {
        this.postLikeCount += 1;
    }

    public void removePostLikeCount() {
        this.postLikeCount -= 1;
    }

//    //==연관관계 메소드==//
//    public void setUser(User user) {
//        this.user = user;
//        user.getPosts().add(this);
//    }

    public void addUserAndCategory(Account account, Category category) {
        this.account = account;
        this.category = category;
    }

    public void addLike(PostLike postLike) {
        postLike.addPost(this);
        this.postLikes.add(postLike);
    }


    public Post(Long id) {
        this.id = id;
    }

    @Builder
    public Post(String title, String content, String code, PostStatus status) {
        this.content = content;
        this.title = title;
        this.status = status;
    }
}
