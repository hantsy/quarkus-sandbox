package com.example;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static com.fasterxml.jackson.databind.SerializationFeature.*;

@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper jackson2ObjectMapperBuilder(){
        Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.json()
                .featuresToEnable(INDENT_OUTPUT)
                .featuresToDisable(WRITE_DATES_AS_TIMESTAMPS);

        return builder.build();
    }
}
