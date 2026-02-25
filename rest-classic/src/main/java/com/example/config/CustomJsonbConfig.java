package com.example.config;

import io.quarkus.jsonb.JsonbConfigCustomizer;
import lombok.extern.slf4j.Slf4j;

import jakarta.inject.Singleton;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.config.PropertyVisibilityStrategy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Singleton
@Slf4j
public class CustomJsonbConfig implements JsonbConfigCustomizer {
    @Override
    public void customize(JsonbConfig jsonbConfig) {
        // To enable ser/des in Record.
        // see: https://dev.to/cchacin/java-14-records-with-jakartaee-json-b-160n
        PropertyVisibilityStrategy propertyVisibilityStrategy = new PropertyVisibilityStrategy() {
            @Override
            public boolean isVisible(Field field) {
                return true;
            }

            @Override
            public boolean isVisible(Method method) {
                return false;
            }
        };
        jsonbConfig.withFormatting(true)
                .withNullValues(false)
                .withPropertyVisibilityStrategy(propertyVisibilityStrategy)
                .withEncoding("UTF-8");
        log.info("customized jsonb config: {}", jsonbConfig.getAsMap());
    }
}
