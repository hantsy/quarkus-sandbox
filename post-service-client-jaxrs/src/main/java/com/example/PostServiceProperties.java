package com.example;

import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "post-service.")
public class PostServiceProperties {
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
