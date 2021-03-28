package com.icloud.my_portfolio.repository;

import com.icloud.my_portfolio.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
}
