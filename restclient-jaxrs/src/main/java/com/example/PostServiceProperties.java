package com.example;

import io.smallrye.config.ConfigMapping;
import org.eclipse.microprofile.config.inject.ConfigProperties;

@ConfigMapping(prefix = "post-service", namingStrategy = ConfigMapping.NamingStrategy.SNAKE_CASE)
public interface PostServiceProperties {
     String baseUrl();
}
