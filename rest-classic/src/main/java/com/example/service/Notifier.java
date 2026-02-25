package com.example.service;

import com.example.domain.Comment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class Notifier {
    private static final Logger LOGGER = Logger.getLogger(Notifier.class.getName());

    public void onCommentAdded(@Observes Comment comment) {
        LOGGER.log(Level.INFO, "comment saved: {0}", comment);
    }
}
