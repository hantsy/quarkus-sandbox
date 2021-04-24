package com.example;

import com.example.domain.Post;
import com.example.repository.PostRepository;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.logging.Logger;

@ApplicationScoped
public class DataInitializer {
    private final static Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());
    
    @Inject
    PostRepository posts;

//    @Inject
//    TransactionManager tm;
//            try {
//        tm.begin();
//        this.posts.persist(first, second);
//        this.posts.flush();
//        tm.commit();
//    }catch (Exception e) {
//        LOGGER.severe(e.getMessage());
//        try {
//            tm.rollback();
//        } catch (SystemException ex) {
//            ex.printStackTrace();
//        }
//    }
    
    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        Post first = Post.builder().title("Hello Quarkus").content("My first post of Quarkus").build();
        Post second = Post.builder().title("Hello Again, Quarkus").content("My second post of Quarkus").build();
        
        this.posts.persist(first, second);
        this.posts.flush();
        
        this.posts.listAll().forEach(p -> System.out.println("Post:" + p));
    }
    
    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
    }
}
