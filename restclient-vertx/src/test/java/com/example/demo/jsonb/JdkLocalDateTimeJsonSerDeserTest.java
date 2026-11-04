package com.example.demo.jsonb;

import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;
import java.util.logging.Logger;

public class JdkLocalDateTimeJsonSerDeserTest {
    private static final Logger LOGGER = Logger.getLogger(JdkLocalDateTimeJsonSerDeserTest.class.getSimpleName());
    JsonMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = JsonMapper.builder()
                .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DateTimeFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .disable(DateTimeFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .build();
    }

    @Test
    public void testSeAndDes() {
        var testObject = new TestObject();
        LOGGER.info("testObject:" + testObject);
        JsonbConfig config = new JsonbConfig()
                .withSerializers(new JsonbLocalDateTimeSerializer());
        var json = JsonbBuilder.create(config).toJson(testObject);
        LOGGER.info("ser by Jsonb:" + json);

       var deObject = objectMapper.readValue(json, TestObject.class);

       LOGGER.info("de object:" + deObject);
    }
}
