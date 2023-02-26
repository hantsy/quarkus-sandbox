package com.example;

import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Logger;

public class PostRepositoryImpl implements PostRepositoryCustom {
    private static final Logger LOGGER = Logger.getLogger(PostRepositoryImpl.class.getName());
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Post> findByKeyword(String q, int offset, int limit) {
        LOGGER.info("q:" + q + ", offset:" + offset + ", limit:" + limit);
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> query = cb.createQuery(Post.class);
        Root<Post> root = query.from(Post.class);
        if (!StringUtils.isEmpty(q)) {
            query.where(
                    cb.or(
                            cb.like(root.get(Post_.title), "%" + q + "%"),
                            cb.like(root.get(Post_.content), "%" + q + "%")
                    )
            );

        }
        return this.entityManager.createQuery(query)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
