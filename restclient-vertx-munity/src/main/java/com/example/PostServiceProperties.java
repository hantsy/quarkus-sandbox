package com.example;

import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "post-service")
public class PostServiceProperties {
    private String host = "localhost";
    private int port = 8080;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
