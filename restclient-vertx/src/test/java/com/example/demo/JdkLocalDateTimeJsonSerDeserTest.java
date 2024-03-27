package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class JdkLocalDateTimeJsonSerDeserTest {
    private static final Logger LOGGER = Logger.getLogger(JdkLocalDateTimeJsonSerDeserTest.class.getSimpleName());
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
        objectMapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);

        JavaTimeModule module = new JavaTimeModule();
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        objectMapper.registerModule(module);
    }

    @Test
    public void testSeAndDes() throws JsonProcessingException {
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

class  JsonbLocalDateTimeSerializer implements JsonbSerializer<TestObject>{
    private static final Logger LOGGER = Logger.getLogger(JsonbLocalDateTimeSerializer.class.getSimpleName());
    @Override
    public void serialize(TestObject a, JsonGenerator generator, SerializationContext ctx) {
        LOGGER.info("LocalDateTime:" + a);
        generator.writeStartObject();
        generator.write("occured", a.getOccured().toString());
        generator.writeEnd();
    }
}

// see: https://github.com/eclipse-ee4j/yasson/issues/629
class TestObject {
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