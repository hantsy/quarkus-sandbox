package org.example;

import io.quarkus.resteasy.reactive.common.runtime.VertxBufferMessageBodyWriter;
import io.vertx.core.buffer.Buffer;
import org.jboss.resteasy.reactive.server.spi.ResteasyReactiveResourceInfo;
import org.jboss.resteasy.reactive.server.spi.ServerMessageBodyWriter;
import org.jboss.resteasy.reactive.server.spi.ServerRequestContext;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Type;

@Provider
public class ServerVertxBufferMessageBodyWriter extends VertxBufferMessageBodyWriter
        implements ServerMessageBodyWriter<Buffer> {

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, ResteasyReactiveResourceInfo resteasyReactiveResourceInfo, MediaType mediaType) {
        return true;
    }

    @Override
    public void writeResponse(Buffer buffer, Type type, ServerRequestContext serverRequestContext) throws WebApplicationException, IOException {
        serverRequestContext.serverResponse().end(buffer.getBytes());
    }
}
