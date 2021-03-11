package com.icloud.my_portfolio.controller.dto;


import com.icloud.my_portfolio.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
