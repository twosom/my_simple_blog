package com.icloud.my_portfolio.category.service;


import com.icloud.my_portfolio.category.Category;
import com.icloud.my_portfolio.post.Post;
import com.icloud.my_portfolio.category.repository.CategoryRepository;
import com.icloud.my_portfolio.post.repository.PostRepositoryWithJpql;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final CategoryRepository categoryRepository;
    private final PostRepositoryWithJpql postRepositoryWithJpql;


    public void createCategory(Category category) {
        category.setCreatedDate(LocalDateTime.now());
        categoryRepository.save(category);
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
        categoryRepository.deleteById(id);
    }

    public void updateCategory(Category category) {
        Category oldCategory = categoryRepository.findById(category.getId()).orElseThrow(RuntimeException::new);
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
        return categoryRepository.findAll();
    }

    public Category findOne(Long id) {
        return categoryRepository.findById(id).orElse(new Category());
    }
}
