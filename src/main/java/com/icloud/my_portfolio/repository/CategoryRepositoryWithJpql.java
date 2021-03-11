package com.icloud.my_portfolio.repository;


import com.icloud.my_portfolio.domain.Category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryWithJpql {

    private final EntityManager em;


    public Category save(Category category) {
        em.persist(category);
        return category;
    }

    public void delete(Long id) {
        Category category = findOne(id);
        em.remove(category);
    }

    public Category findOne(Long id) {
        return em.find(Category.class, id);
    }

    public List<Category> findAll() {
        return em.createQuery(
                "select c " +
                        "from Category c ", Category.class)
                .getResultList();
    }
}
