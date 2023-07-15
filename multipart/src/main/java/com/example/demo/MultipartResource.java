package com.example.demo;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataOutput;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("")
public class MultipartResource {
    public static final Logger LOGGER = Logger.getLogger(MultipartResource.class.getName());

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public String upload(MultipartBody data) throws IOException {
        LOGGER.log(Level.INFO, "data: {0}", data);
        var uploaded = data.getPart();
        LOGGER.log(Level.INFO, "test field: {0}", data.getTest());
        LOGGER.log(Level.INFO, "part field: {0}", uploaded.getName());
        LOGGER.log(Level.INFO, "checked: {0}", data.isChecked());
        LOGGER.log(Level.INFO, "choice: {0}", data.getChoice());
        return "ok";
    }

    @POST
    @Path("pojo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response pojo(SimplePoJo data) {
        LOGGER.log(Level.INFO, "data: {0}", data);
        data.setUpdatedAt(LocalDateTime.now());
        return Response.ok(data).build();
    }

    @POST
    @Path("upload2")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public String upload2(MultipartFormDataInput input) throws IOException {

        return "ok";
    }

    @POST()
    @Path("bin")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.TEXT_PLAIN)
    public String upload(byte[] data) throws IOException {
        LOGGER.log(Level.INFO, "converting bin body to string:\n{0}", ByteBuffer.wrap(data).toString());
        return "ok";
    }


    @GET()
    @Path("output")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public MultipartFormDataOutput output() throws IOException {
        MultipartFormDataOutput out = new MultipartFormDataOutput();
        out.addFormData("user", UserData.of("jack", "ma"), MediaType.APPLICATION_JSON_TYPE);
        out.addFormData("user", Product.of("Alibaba", new BigDecimal(1000_000_000)), MediaType.APPLICATION_XML_TYPE);
        return out;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    static class UserData {
        String firstName;
        String lastName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    static class Product {
        String name;
        BigDecimal price;
    }
}