package com.example;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "post-service")
public interface PostServiceProperties {
    String host();

    int port();
}
