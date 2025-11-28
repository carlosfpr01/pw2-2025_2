package br.com.gateway.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@RegisterRestClient(configKey = "users-api")
public interface UsersClient {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    Uni<Response> login(Object loginRequest);

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Response> createUser(Object createUserRequest);

    @PATCH
    @Path("/updateUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Response> updateUser(@HeaderParam("Authorization") String authHeader, Object updateUserRequest);

    @GET
    @Path("/getUsers")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Response> getUsers(@HeaderParam("Authorization") String authHeader);

    @GET
    @Path("/getUser")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Response> getUser(@HeaderParam("Authorization") String authHeader);
}
