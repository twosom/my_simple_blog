package com.icloud.my_portfolio.repository;


import com.icloud.my_portfolio.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByEmail(String email);

    List<User> findByUsername(String username);


    @Query(" SELECT u " +
            "FROM User u " +
            "WHERE u.username = :username")
    Long findUserIdByUsername(@Param("username") String username);
}
