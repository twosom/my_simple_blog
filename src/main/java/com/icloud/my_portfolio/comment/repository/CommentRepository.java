package com.icloud.my_portfolio.comment.repository;

import com.icloud.my_portfolio.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
