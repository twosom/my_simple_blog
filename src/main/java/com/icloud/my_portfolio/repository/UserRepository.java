package com.icloud.my_portfolio.repository;


import com.icloud.my_portfolio.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    /* 저장, 조회, 업데이트, 삭제 */

    public User save(User user) {
        if (user.getId() == null) {
            em.persist(user);
        } else {
            System.out.println(user);
            em.merge(user);
        }
        return user;
    }


    public List<User> findByEmail(String email) {
        return em.createQuery(
                "select u " +
                        "from User u " +
                        "where u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();
    }

    public List<User> findByName(String username) {
        return em.createQuery(
                "select u " +
                        "from User u " +
                        "where u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();
    }
}
