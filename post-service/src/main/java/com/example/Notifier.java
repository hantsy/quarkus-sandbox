package com.example;

import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
@Slf4j
public class Notifier {
    
    public void onCommentAdded(@Observes Comment comment){
        log.info("comment saved: {}", comment);
    }
}
