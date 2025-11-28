package br.com.gateway.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/gastos")
@RegisterRestClient(configKey = "gastos-api")
public interface GastosClient {

    @GET
    @Path("/test-auth")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Response> testAuth(@HeaderParam("Authorization") String authHeader);

    @POST
    @Path("/despesa/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Response> createDespesa(@HeaderParam("Authorization") String authHeader, Object createDespesaRequest);

    @GET
    @Path("/despesa/sumario")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Response> getSumario(
        @HeaderParam("Authorization") String authHeader,
        @QueryParam("startDate") String startDate,
        @QueryParam("endDate") String endDate
    );

    @GET
    @Path("/despesa/sumarioTag")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Response> getSumarioTag(
        @HeaderParam("Authorization") String authHeader,
        @QueryParam("tag") String tag,
        @QueryParam("startDate") String startDate,
        @QueryParam("endDate") String endDate
    );

    @GET
    @Path("/despesa/listDespesas")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Response> listDespesas(
        @HeaderParam("Authorization") String authHeader,
        @QueryParam("startDate") String startDate,
        @QueryParam("endDate") String endDate
    );

    @GET
    @Path("/despesa/listTagSum")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Response> listTagSum(
        @HeaderParam("Authorization") String authHeader,
        @QueryParam("startDate") String startDate,
        @QueryParam("endDate") String endDate
    );

    @PATCH
    @Path("/despesa/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Response> updateDespesa(@HeaderParam("Authorization") String authHeader, Object updateRequest);

    @DELETE
    @Path("/despesa/delete")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Response> deleteDespesa(
        @HeaderParam("Authorization") String authHeader,
        @QueryParam("id") Long id
    );
}
