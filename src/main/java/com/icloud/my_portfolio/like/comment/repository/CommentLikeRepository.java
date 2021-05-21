package com.icloud.my_portfolio.like.comment.repository;

import com.icloud.my_portfolio.like.comment.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
}
