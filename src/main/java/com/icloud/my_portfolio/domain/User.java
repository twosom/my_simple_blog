package com.icloud.my_portfolio.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;

    private String name;


//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//    private List<Post> posts = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//    private List<Comment> comments = new ArrayList<>();
}
