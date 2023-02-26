package com.example.web;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.ext.Providers;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@Provider
public class StreamBodyWriter implements MessageBodyWriter<Stream> {

    @Context
    private Providers providers;

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
                               Annotation[] annotations, MediaType mediaType) {
        return Stream.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(Stream stream, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Stream stream, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {

        Object obj = stream.collect(Collectors.toList());
        Class<?> objType = obj.getClass();

        MessageBodyWriter writer = providers.getMessageBodyWriter(objType,
                null, annotations, mediaType);

        writer.writeTo(obj, objType, null, annotations,
                mediaType, httpHeaders, entityStream);

    }
}