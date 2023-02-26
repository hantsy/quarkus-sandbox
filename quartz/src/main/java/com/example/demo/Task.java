package com.example.demo;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "TASKS")
public class Task extends PanacheEntity {
    public Instant createdAt;

    public Task() {
        createdAt = Instant.now();
    }

    public Task(Instant time) {
        this.createdAt = time;
    }
}