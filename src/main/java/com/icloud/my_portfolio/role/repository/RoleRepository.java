package com.icloud.my_portfolio.role.repository;

import com.icloud.my_portfolio.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(String roleName);

    @Query(
            "SELECT r " +
            "FROM Role r " +
            "ORDER BY r.roleGrade ASC")
    List<Role> findAllRolesByRoleGrade();
}
