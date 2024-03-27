package com.example.demo;

import java.time.LocalDateTime;

class TestLocalObject {
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
