package com.icloud.my_portfolio.repository;


import com.icloud.my_portfolio.domain.Post;
import com.icloud.my_portfolio.domain.PostStatus;
import com.icloud.my_portfolio.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryWithJpql {

    private final EntityManager em;

    public Post findByIdAndStatus(Long id, PostStatus status) {
        List<Post> resultList = em.createQuery(
                "select p " +
                        "from Post p " +
                        "where p.id = :id and p.status = :status", Post.class)
                .setParameter("id", id)
                .setParameter("status", status)
                .getResultList();
        if (resultList.isEmpty()) {
            throw new PostNotFoundException(id + "번 게시글을  찾지 못했습니다.");
        }
        return resultList.get(0);
    }

    public Post save(Post post) {
        if (post.getId() == null) {
            em.persist(post);
        } else {
            em.merge(post);
        }

        return post;
    }

    public List<Post> findPostWithCategory(Long id) {
        return em.createQuery(
                "select p " +
                        "from Post p join p.category c " +
                        "where c.id = :id", Post.class)
                .setParameter("id", id)
                .getResultList();
    }
}
