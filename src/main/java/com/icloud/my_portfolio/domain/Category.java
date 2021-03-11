package com.icloud.my_portfolio.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Category extends SuperClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

    public Category(Long id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "category")
    private List<Post> posts = new ArrayList<>();

    @Builder
    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
