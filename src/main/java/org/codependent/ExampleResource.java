package org.codependent;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/echo")
public class ExampleResource {

    private final RestClient restClient;

    public ExampleResource(RestClient restClient){
        this.restClient = restClient;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response get() {
        return restClient.get();
    }
}
