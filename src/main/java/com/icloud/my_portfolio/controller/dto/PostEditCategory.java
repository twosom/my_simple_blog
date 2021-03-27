package com.icloud.my_portfolio.controller.dto;

import com.icloud.my_portfolio.domain.Category;
import lombok.Data;

@Data
public class PostEditCategory {

    private Long id;
    private String name;

    public PostEditCategory(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }
}
