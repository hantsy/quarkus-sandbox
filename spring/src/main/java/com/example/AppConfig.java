package com.example;


import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;
import io.quarkus.jackson.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class AppConfig {

    @Bean
    public JsonMapperBuilderCustomizer objectMapperCustomizer() {
        return (JsonMapper.Builder builder) -> {
            builder.disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS);
            builder.disable(DateTimeFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
            builder.disable(DateTimeFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
        };
    }

}
