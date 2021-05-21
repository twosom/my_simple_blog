package com.icloud.my_portfolio.category.dto;


import com.icloud.my_portfolio.category.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
@NoArgsConstructor
public class CategoryDto {

    private Long id;

    @NotEmpty(message = "카테고리 명은 필수입니다.")
    private String name;

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }

    public Category toEntity() {
        return Category.builder()
                .id(getId())
                .name(getName())
                .build();
    }
}
