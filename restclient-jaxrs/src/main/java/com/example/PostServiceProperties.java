package com.example;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "post-service", namingStrategy = ConfigMapping.NamingStrategy.SNAKE_CASE)
public interface PostServiceProperties {
    String baseUrl();
}
