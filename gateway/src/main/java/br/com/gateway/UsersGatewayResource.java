package br.com.gateway;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.com.gateway.client.UsersClient;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Users", description = "Gerenciamento de usuários e autenticação")
public class UsersGatewayResource {

    @Inject
    @RestClient
    UsersClient usersClient;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(summary = "Login de usuário", description = "Autentica um usuário e retorna um token JWT")
    @APIResponse(responseCode = "200", description = "Login bem-sucedido, retorna JWT token")
    @APIResponse(responseCode = "401", description = "Credenciais inválidas")
    public Uni<Response> login(Object loginRequest) {
        return usersClient.login(loginRequest);
    }

    @POST
    @Path("/create")
    @Operation(summary = "Criar usuário", description = "Cria um novo usuário no sistema")
    @APIResponse(responseCode = "200", description = "Usuário criado com sucesso")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    public Uni<Response> createUser(Object createUserRequest) {
        return usersClient.createUser(createUserRequest);
    }

    @PATCH
    @Path("/updateUser")
    public Uni<Response> updateUser(@HeaderParam("Authorization") String authHeader, Object updateUserRequest) {
        return usersClient.updateUser(authHeader, updateUserRequest);
    }

    @GET
    @Path("/getUsers")
    public Uni<Response> getUsers(@HeaderParam("Authorization") String authHeader) {
        return usersClient.getUsers(authHeader);
    }

    @GET
    @Path("/getUser")
    public Uni<Response> getUser(@HeaderParam("Authorization") String authHeader) {
        return usersClient.getUser(authHeader);
    }
}
