package com.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import io.quarkus.jackson.ObjectMapperCustomizer;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class CustomObjectMapper implements ObjectMapperCustomizer {

    @Override
    public void customize(ObjectMapper objectMapper) {
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
        objectMapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);

        JavaTimeModule module = new JavaTimeModule();
        LocalDateTimeDeserializer localDateTimeDeserializer =  new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        objectMapper.registerModule(module);
    }
}
