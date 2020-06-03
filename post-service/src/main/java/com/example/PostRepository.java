package com.example;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.orm.panache.runtime.JpaOperations;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PostRepository implements PanacheRepositoryBase<Post, String> {

    public List<Post> findAllPosts() {
        return this.listAll(Sort.descending("createdAt"));
    }

//    public Stream<Post> findByKeyword(String q, int offset, int limit) {
//        if (q == null || q.trim().isEmpty()) {
//            return this.streamAll(Sort.descending("createdAt"))
//                    .skip(offset)
//                    .limit(limit);
//        } else {
//            return this.streamAll(Sort.descending("createdAt"))
//                    .filter(p -> p.title.contains(q) || p.content.contains(q))
//                    .skip(offset)
//                    .limit(limit);
//        }
//    }

    public List<Post> findByKeyword(String q, int offset, int limit) {
        if (q == null || q.trim().isEmpty()) {
            return this.findAll(Sort.descending("createdAt"))
                    .page(offset / limit, limit)
                    .list();
        } else {
            return this.find("title like ?1 or content like ?1", Sort.descending("createdAt"), '%' + q + '%')
                    .page(offset / limit, limit)
                    .list();
        }
    }

    public Long countByKeyword(String q) {
        if (q == null || q.trim().isEmpty()) {
            return this.count();
        } else {
            return this.count("title like ?1 or content like ?1", '%' + q + '%');
        }
    }

    public Optional<Post> getById(String id) {
        Post post = null;
        try {
            post = this.find("id=:id", Parameters.with("id", id)).singleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(post);
    }

    @Transactional
    public Post save(Post post) {
        EntityManager em = JpaOperations.getEntityManager();
        if (post.getId() == null) {
            em.persist(post);
            return post;
        } else {
            return em.merge(post);
        }
    }

}
