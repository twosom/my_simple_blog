package com.icloud.my_portfolio.service;


import com.icloud.my_portfolio.domain.Category;
import com.icloud.my_portfolio.domain.Post;
import com.icloud.my_portfolio.repository.CategoryRepository;
import com.icloud.my_portfolio.repository.CategoryRepositoryWithJpql;
import com.icloud.my_portfolio.repository.PostRepositoryWithJpql;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepositoryWithJpql categoryRepositoryWithJpql;
    private final CategoryRepository categoryRepository;
    private final PostRepositoryWithJpql postRepositoryWithJpql;


    public Category createCategory(Category category) {
        category.setCreatedDate(LocalDateTime.now());
        return categoryRepositoryWithJpql.save(category);
    }

    public void delete(Long id) {
        /* 만약 카테고리와 연관관계를 맺고 있는 포스트가 있으면 연관관계 제거 위해서
         *  카테고리 아이디와 연관된 모든 포스트 찾아오기 */
        List<Post> postWithCategory = postRepositoryWithJpql.findPostWithCategory(id);
        if (!postWithCategory.isEmpty()) {
            postWithCategory.forEach(
                    board -> board.setCategory(null)
            );
        }
        categoryRepositoryWithJpql.delete(id);
    }

    public void updateCategory(Category category) {
        Category oldCategory = categoryRepositoryWithJpql.findOne(category.getId());
        if (oldCategory != null) {
            oldCategory.setName(category.getName());
        }
    }

    @Transactional(readOnly = true)
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepositoryWithJpql.findAll();
    }

    public Category findOne(Long id) {
        return categoryRepositoryWithJpql.findOne(id);
    }
}
