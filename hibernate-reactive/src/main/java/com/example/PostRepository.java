package com.example;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.reactive.mutiny.Mutiny;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PostRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostRepository.class);

    private final Mutiny.SessionFactory sessionFactory;

    @Inject
    public PostRepository(Mutiny.SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Uni<List<Post>> findAll() {

        return this.sessionFactory.withSession(session -> {
                    var cb = this.sessionFactory.getCriteriaBuilder();
                    var query = cb.createQuery(Post.class);
                    var root = query.from(Post.class);
                    return session.createQuery(query).getResultList();
                }
        );

    }

    public Uni<Post> findById(UUID id) {
        return this.sessionFactory.withSession(session -> session.find(Post.class, id))
                .onItem().ifNull().failWith(new PostNotFoundException(id));
    }

    public Uni<UUID> save(Post data) {
        return this.sessionFactory.withTransaction((session, transaction) ->
                session.persist(data).map(v -> data.getId())
        );
    }

    public Uni<Integer> update(UUID id, Post data) {
        return this.sessionFactory.withTransaction((session, transaction) -> {
            //create CriteriaBuilder
            var cb = this.sessionFactory.getCriteriaBuilder();

            // create CriteriaUpdate
            var update = cb.createCriteriaUpdate(Post.class);

            // create root
            var root = update.from(Post.class);

            // set where clause
            update.set(root.get(Post_.title), data.getTitle());
            update.set(root.get(Post_.content), data.getContent());
            update.where(cb.equal(root.get(Post_.id), id));

            return session.createQuery(update).executeUpdate();
        });
    }

    public Uni<Integer> deleteAll() {
        return this.sessionFactory.withTransaction((session, transaction) -> {
            var cb = this.sessionFactory.getCriteriaBuilder();
            var delete = cb.createCriteriaDelete(Post.class);
            var root = delete.from(Post.class);

            return session.createQuery(delete).executeUpdate();
        });
    }

    public Uni<Integer> delete(UUID id) {
        return this.sessionFactory.withTransaction((session, transaction) -> {
            //create CriteriaBuilder
            var cb = this.sessionFactory.getCriteriaBuilder();

            // create CriteriaDelete
            var delete = cb.createCriteriaDelete(Post.class);

            // create root
            var root = delete.from(Post.class);

            // set where clause
            delete.where(cb.equal(root.get(Post_.id), id));

            return session.createQuery(delete).executeUpdate();
        });
    }

}
