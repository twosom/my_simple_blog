package com.icloud.my_portfolio.repository;

import com.icloud.my_portfolio.domain.Post;
import com.icloud.my_portfolio.domain.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {


    @EntityGraph(attributePaths = {"category", "user"})
    Page<Post> findAllByStatusOrderByCreatedDateDesc(PostStatus status, Pageable pageable);


}
