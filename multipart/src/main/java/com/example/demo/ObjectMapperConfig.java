package com.example.demo;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;
import io.quarkus.jackson.JsonMapperBuilderCustomizer;

import jakarta.inject.Singleton;

@Singleton
public class ObjectMapperConfig implements JsonMapperBuilderCustomizer {

    @Override
    public void customize(JsonMapper.Builder builder) {
        // To suppress serializing properties with null values
        builder.changeDefaultPropertyInclusion(incl -> incl
                .withValueInclusion(JsonInclude.Include.NON_EMPTY)
                .withContentInclusion(JsonInclude.Include.NON_EMPTY));
        builder.disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS);
        builder.disable(DateTimeFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
        builder.disable(DateTimeFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
    }
}
