package com.icloud.my_portfolio.category.repository;

import com.icloud.my_portfolio.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
