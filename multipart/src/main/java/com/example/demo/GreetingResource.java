package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("")
public class GreetingResource {
    public static final Logger LOGGER = Logger.getLogger(GreetingResource.class.getName());

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public String upload(@MultipartForm MultipartSampleData data) throws IOException {
        var uploaded = data.getPart();
        var content = IOUtils.toString(uploaded, StandardCharsets.UTF_8);
        LOGGER.log(Level.INFO, "test field: {0}", data.getTest());
        LOGGER.log(Level.INFO, "part field: {0}", content);

        return content;
    }

    @POST
    @Path("upload2")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public String upload2(MultipartFormDataInput input) throws IOException {
        input.getParts().forEach(
                part-> {
                    try {
                        LOGGER.log(Level.INFO, "part:\n{0}", new Object[]{part.getBodyAsString()});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        return "ok";
    }

    @POST()
    @Path("bin")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.TEXT_PLAIN)
    public String upload(byte[] data) throws IOException {
        var content = IOUtils.toString(data, StandardCharsets.UTF_8.toString());
        LOGGER.log(Level.INFO, "converting bin body to string:\n{0}", content);
        return content;
    }


    @GET()
    @Path("output")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public MultipartFormDataOutput output() throws IOException {
        MultipartFormDataOutput out = new MultipartFormDataOutput();
        out.addFormData("user",  UserData.of("jack", "ma"), MediaType.APPLICATION_JSON_TYPE );
        out.addFormData("user",  Product.of("Alibaba", new BigDecimal(1000_000_000)), MediaType.APPLICATION_XML_TYPE );
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
    static class Product{
        String name;
        BigDecimal price;
    }
}