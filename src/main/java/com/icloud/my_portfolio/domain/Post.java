package com.icloud.my_portfolio.domain;


import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@ToString(of = {"id", "content", "title", "status"})
public class Post extends SuperClass{

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
    @JoinColumn(name=  "category_id")
    private Category category;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostLike> postLikes = new ArrayList<>();

//    //==연관관계 메소드==//
//    public void setUser(User user) {
//        this.user = user;
//        user.getPosts().add(this);
//    }

    public void addUserAndCategory(User user, Category category) {
        this.user = user;
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
