package br.com.gateway;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.com.gateway.client.GastosClient;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
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

@Path("/api/gastos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Gastos", description = "Gerenciamento de despesas e receitas")
public class GastosGatewayResource {

    @Inject
    @RestClient
    GastosClient gastosClient;

    @GET
    @Path("/test-auth")
    public Uni<Response> testAuth(@HeaderParam("Authorization") String authHeader) {
        return gastosClient.testAuth(authHeader);
    }

    @POST
    @Path("/despesa/create")
    public Uni<Response> createDespesa(@HeaderParam("Authorization") String authHeader, Object createDespesaRequest) {
        return gastosClient.createDespesa(authHeader, createDespesaRequest);
    }

    @GET
    @Path("/despesa/sumario")
    public Uni<Response> getSumario(
        @HeaderParam("Authorization") String authHeader,
        @QueryParam("startDate") String startDate,
        @QueryParam("endDate") String endDate
    ) {
        return gastosClient.getSumario(authHeader, startDate, endDate);
    }

    @GET
    @Path("/despesa/sumarioTag")
    public Uni<Response> getSumarioTag(
        @HeaderParam("Authorization") String authHeader,
        @QueryParam("tag") String tag,
        @QueryParam("startDate") String startDate,
        @QueryParam("endDate") String endDate
    ) {
        return gastosClient.getSumarioTag(authHeader, tag, startDate, endDate);
    }

    @GET
    @Path("/despesa/listDespesas")
    public Uni<Response> listDespesas(
        @HeaderParam("Authorization") String authHeader,
        @QueryParam("startDate") String startDate,
        @QueryParam("endDate") String endDate
    ) {
        return gastosClient.listDespesas(authHeader, startDate, endDate);
    }

    @GET
    @Path("/despesa/listTagSum")
    public Uni<Response> listTagSum(
        @HeaderParam("Authorization") String authHeader,
        @QueryParam("startDate") String startDate,
        @QueryParam("endDate") String endDate
    ) {
        return gastosClient.listTagSum(authHeader, startDate, endDate);
    }

    @PATCH
    @Path("/despesa/update")
    public Uni<Response> updateDespesa(@HeaderParam("Authorization") String authHeader, Object updateRequest) {
        return gastosClient.updateDespesa(authHeader, updateRequest);
    }

    @DELETE
    @Path("/despesa/delete")
    public Uni<Response> deleteDespesa(
        @HeaderParam("Authorization") String authHeader,
        @QueryParam("id") Long id
    ) {
        return gastosClient.deleteDespesa(authHeader, id);
    }
}
