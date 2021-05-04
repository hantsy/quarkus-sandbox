package com.example.demo;

import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class TaskBean {

    @Transactional
    @Scheduled(every = "10s", identity = "task-job")
    void schedule() {
        Task task = new Task();
        task.persist();
    }
}