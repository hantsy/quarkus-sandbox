package com.example;

import org.hibernate.SessionFactory;
import org.hibernate.reactive.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class PostRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostRepository.class);

    private final Stage.Session session;
    private final SessionFactory sessionFactory;

    @Inject
    public PostRepository(Stage.Session _client, SessionFactory sessionFactory) {
        this.session = _client;
        this.sessionFactory = sessionFactory;
    }

    public CompletionStage<List<Post>> findAll() {
        var query = this.sessionFactory.getCriteriaBuilder().createQuery(Post.class);
        var root = query.from(Post.class);
        return this.session.createQuery(query).getResultList();
    }


    public CompletionStage<Post> findById(UUID id) {
        return this.session.find(Post.class, id);
    }

    public CompletionStage<UUID> save(Post data) {
        return this.session.persist(data).thenApply(v -> data.getId());
    }

    public CompletionStage<Integer> update(UUID id, Post data) {

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

        return this.session.createQuery(update).executeUpdate();

    }

    public CompletionStage<Integer> deleteAll() {
        //create CriteriaBuilder
        var cb = this.sessionFactory.getCriteriaBuilder();

        // create CriteriaDelete
        var delete = cb.createCriteriaDelete(Post.class);

        // create root
        var root = delete.from(Post.class);

        return this.session.createQuery(delete).executeUpdate();

    }

    public CompletionStage<Integer> delete(String id) {

        //create CriteriaBuilder
        var cb = this.sessionFactory.getCriteriaBuilder();

        // create CriteriaDelete
        var delete = cb.createCriteriaDelete(Post.class);

        // create root
        var root = delete.from(Post.class);

        // set where clause
        delete.where(cb.equal(root.get(Post_.id), id));

        return this.session.createQuery(delete).executeUpdate();
    }

}
