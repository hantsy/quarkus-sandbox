package com.example.config;

import io.quarkus.jsonb.JsonbConfigCustomizer;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import javax.json.bind.JsonbConfig;

@Singleton
@Slf4j
public class CustomJsonbConfig implements JsonbConfigCustomizer {
    @Override
    public void customize(JsonbConfig jsonbConfig) {
        jsonbConfig.withFormatting(true).withEncoding("UTF-8");
        log.info("customized jsonb config: {}", jsonbConfig.getAsMap());
    }
}
