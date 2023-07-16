package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@QuarkusTest
public class ObjectMapperTest {
    private static final Logger LOGGER = Logger.getLogger(ObjectMapperTest.class.getSimpleName());
    @Inject
    ObjectMapper objectMapper;

//    @BeforeEach
//    public void setup() {
//        objectMapper = new ObjectMapper();
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        objectMapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
//        objectMapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
//
//        JavaTimeModule module = new JavaTimeModule();
//        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
//        objectMapper.registerModule(module);
//    }

    @Test
    public void testObjectMapper() throws JsonProcessingException {
        var json = objectMapper.writeValueAsString(new TestLocalObject());
        LOGGER.info("serialized result:" + json);

        var deObject = objectMapper.readValue(json, TestLocalObject.class);
        LOGGER.info("deserialized ojb:" + deObject);
    }
}

class TestLocalObject {
    public LocalDateTime occured = LocalDateTime.now();

    public LocalDateTime getOccured() {
        return occured;
    }

    public void setOccured(LocalDateTime occured) {
        this.occured = occured;
    }

    @Override
    public String toString() {
        return "TestObject{" +
                "occured=" + occured +
                '}';
    }
}