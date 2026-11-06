package com.example.demo.jsonb;

import java.time.LocalDateTime;

// see: https://github.com/eclipse-ee4j/yasson/issues/629
public class TestObject {
    public LocalDateTime occured = LocalDateTime.now();

    public LocalDateTime getOccured() {
        return occured;
    }

    public void setOccured(LocalDateTime occured) {
        this.occured = occured;
    }

    @Override
    public String toString() {
        return "TestObject{" +
                "occured=" + occured +
                '}';
    }
}
