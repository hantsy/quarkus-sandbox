package com.example;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.hibernate.orm.panache.runtime.JpaOperations;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class CommentRepository implements PanacheRepositoryBase<Comment, String> {

    @Transactional
    public Comment save(Comment comment) {
        EntityManager em = JpaOperations.getEntityManager();
        if (comment.getId() == null || comment.getId().trim().isEmpty()) {
            em.persist(comment);
            return comment;
        } else {
            return em.merge(comment);
        }
    }

    @Transactional
    public void deleteById(String id) {
        this.delete("id", id);
    }

    public List<Comment> allByPostId(String id) {
        return this.list("post.id", id);
    }
}
