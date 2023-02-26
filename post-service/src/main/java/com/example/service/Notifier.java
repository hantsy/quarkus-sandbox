package com.example.service;

import com.example.domain.Comment;
import lombok.extern.slf4j.Slf4j;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
@Slf4j
public class Notifier {
    
    public void onCommentAdded(@Observes Comment comment){
        log.info("comment saved: {}", comment);
    }
}
