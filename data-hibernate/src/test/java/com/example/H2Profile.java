package com.example;

import io.quarkus.test.junit.QuarkusTestProfile;

public class H2Profile implements QuarkusTestProfile {

    @Override
    public String getConfigProfile() {
        return "h2";
    }
}