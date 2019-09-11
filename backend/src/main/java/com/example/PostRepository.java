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
import java.util.stream.Stream;

@ApplicationScoped
public class PostRepository implements PanacheRepositoryBase<Post, String> {

    public List<Post> findAllPosts() {
        return this.listAll(Sort.descending("createdAt"));
    }

    public Stream<Post> findByKeyword(String q, int offset, int size) {
        if (q == null || q.trim().isEmpty()) {
            return this.streamAll(Sort.descending("createdAt"))
                    .skip(offset)
                    .limit(size);
        } else {
            return this.streamAll(Sort.descending("createdAt"))
                    .filter(p -> p.title.contains(q) || p.content.contains(q))
                    .skip(offset)
                    .limit(size);
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

    @Transactional
    public void deleteById(String id) {
        this.delete("id=?1", id);
    }
}
