package com.icloud.my_portfolio.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User extends SuperClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    private String password;

    @Column(nullable = false)
    private Boolean enabled;

    @Enumerated(EnumType.STRING)
    private Role role;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

//    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, optional = false)
//    private PostLike postLike;


    @Builder
    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(Long userId) {
        this.id = userId;
    }


    //==객체지향 관점의 연관관계 메소드==//
    public void addComment(Comment comment) {
        comment.setUser(this);
        getComments().add(comment);
    }

//    public void addPostLike(PostLike postLike) {
//        this.postLike = postLike;
//    }
}
