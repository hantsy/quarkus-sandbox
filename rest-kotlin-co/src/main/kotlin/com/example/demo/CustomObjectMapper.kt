package com.example.demo

import tools.jackson.databind.cfg.DateTimeFeature
import tools.jackson.databind.json.JsonMapper
import io.quarkus.jackson.JsonMapperBuilderCustomizer
import jakarta.enterprise.context.ApplicationScoped


@ApplicationScoped
class CustomObjectMapper : JsonMapperBuilderCustomizer {
    override fun customize(builder: JsonMapper.Builder) {
        builder.disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
        builder.disable(DateTimeFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
        builder.disable(DateTimeFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
    }
}
