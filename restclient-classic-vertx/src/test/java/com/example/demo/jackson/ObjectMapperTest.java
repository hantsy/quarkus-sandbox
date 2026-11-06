package com.example.demo.jackson;

import tools.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import java.util.logging.Logger;

@QuarkusTest
public class ObjectMapperTest {
    private static final Logger LOGGER = Logger.getLogger(ObjectMapperTest.class.getSimpleName());
    @Inject
    ObjectMapper objectMapper;

    @Test
    public void testObjectMapper() {
        var json = objectMapper.writeValueAsString(new TestLocalObject());
        LOGGER.info("serialized result:" + json);

        var deObject = objectMapper.readValue(json, TestLocalObject.class);
        LOGGER.info("deserialized ojb:" + deObject);
    }
}
