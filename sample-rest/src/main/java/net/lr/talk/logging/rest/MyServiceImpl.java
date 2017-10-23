package net.lr.talk.logging.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("")
@Produces(MediaType.TEXT_PLAIN)
public class MyServiceImpl {

    @GET
    @Path("/{msg}")
    public Response echo(@PathParam("msg") String message) {
        if ("coffee".equals(message)) {
            return Response.status(418).entity("I am a teapot").build();
        }
        return Response.ok(message).build();
    }
}
