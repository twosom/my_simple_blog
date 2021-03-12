package com.icloud.my_portfolio.repository;

import com.icloud.my_portfolio.domain.Post;
import com.icloud.my_portfolio.domain.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


//==페이징 처리를 위한 SpringDataJPA 클래스==//
public interface PostRepository extends JpaRepository<Post, Long> {

}
