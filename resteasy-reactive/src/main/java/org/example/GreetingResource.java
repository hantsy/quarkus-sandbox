package org.example;

import org.jboss.resteasy.reactive.*;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }

    @POST
    @Path("params/{p}")
    public String params(@RestPath String p,
                         @RestQuery String q,
                         @RestHeader int h,
                         @RestForm String f,
                         @RestMatrix String m,
                         @RestCookie String c) {
        return "params: p: " + p + ", q: " + q + ", h: " + h + ", f: " + f + ", m: " + m + ", c: " + c;
    }

    @POST
    @Path("simpleparams/{p}")
    public String params(String p, UriInfo info) {
        return "params: p: " + p + ", info: " + info;
    }
}

