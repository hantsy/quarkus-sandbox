package com.example.demo.jsonb;

import java.util.logging.Logger;

class JsonbLocalDateTimeSerializer implements JsonbSerializer<TestObject> {
    private static final Logger LOGGER = Logger.getLogger(JsonbLocalDateTimeSerializer.class.getSimpleName());

    @Override
    public void serialize(TestObject a, JsonGenerator generator, SerializationContext ctx) {
        LOGGER.info("LocalDateTime:" + a);
        generator.writeStartObject();
        generator.write("occured", a.getOccured().toString());
        generator.writeEnd();
    }
}
