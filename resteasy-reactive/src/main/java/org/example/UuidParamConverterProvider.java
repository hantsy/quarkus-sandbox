package org.example;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.UUID;

@Provider
public class UuidParamConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {

        if (rawType.getName().equals(UUID.class.getName())) {

            return new ParamConverter<T>() {
                @Override
                public T fromString(String value) {
                    return (T) UUID.fromString(value);
                }

                @Override
                public String toString(T value) {
                    return value.toString();
                }
            };

        }
        return null;
    }
}
