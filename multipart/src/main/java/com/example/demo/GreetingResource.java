package com.example.demo;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.annotations.providers.multipart.PartType;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

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
    public String upload(@MultipartForm MultiformData data) throws IOException {
        var uploaded = data.getPart();
        var content = IOUtils.toString(uploaded, StandardCharsets.UTF_8);
        LOGGER.log(Level.INFO, "test field: {0}",data.getTest());
        LOGGER.log(Level.INFO, "part field: {0}", content);
        
        return content;
    }
}