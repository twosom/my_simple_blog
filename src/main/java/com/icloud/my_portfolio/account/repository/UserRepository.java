package com.icloud.my_portfolio.account.repository;

import com.icloud.my_portfolio.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);

    Account findByEmail(String email);

    @Query(
            "SELECT u.id " +
            "FROM Account u " +
            "WHERE u.username = :username")
    Long findIdByUsername(@Param("username") String username);
}
