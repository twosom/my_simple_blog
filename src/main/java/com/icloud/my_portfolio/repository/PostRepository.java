package com.icloud.my_portfolio.repository;

import com.icloud.my_portfolio.domain.Post;
import com.icloud.my_portfolio.domain.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {


    @Query(value = "select p from Post p where p.status = :status order by p.createdDate desc")
    Page<Post> findAllByStatus(@Param("status") PostStatus status, Pageable pageable);
}
