package org.codependent;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class RestClient {

    private final Client httpClient;

    public RestClient() {
        this.httpClient = ResteasyClientBuilder.newBuilder().build();
    }

    public Response get() {
        return httpClient.target("https://postman-echo.com/get").request().get();
    }

}
