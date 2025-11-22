package dev.ifrs;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dev.ifrs.model.User;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UsersResource {

    private static final String ISSUER = "users-issuer";
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    JsonWebToken jwt;

    private String hashPassword(String password) {
        return encoder.encode(password);
    }

    private boolean verifyPassword(String password, String hash) {
        return encoder.matches(password, hash);
    }


    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @WithTransaction
    public Uni<Response> getToken(LoginRequest request) {
        return User.<User>find("email", request.email).firstResult()
            .onItem().transform(user -> {
                if (user != null && verifyPassword(request.password, user.getPassword())) {
                    String token = Jwt.issuer(ISSUER)
                        .upn(user.getEmail())
                        .groups("user")
                        .claim(Claims.nickname, user.getName())
                        .claim("id", user.id)
                        .claim(Claims.email, user.getEmail())
                        .sign();
                    return Response.ok(token).build();
                } else {
                    return Response.status(Response.Status.UNAUTHORIZED).entity("Credenciais inválidas").build();
                }
            });
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    public Uni<User> createUser(CreateUserRequest request) {
        Log.info("Creating user: " + request.name + " with email: " + request.email);
        User user = new User();
        user.setName(request.name);
        user.setEmail(request.email);
        user.setPassword(hashPassword(request.password));
        user.setDataCriacao(java.time.LocalDateTime.now().toString());
        user.setBalance(request.balance);
        return user.persistAndFlush();
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    public Uni<List<User>> listUser() {
        return User.findAll().list();
    }


    @PATCH
    @Path("/updateUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user"})
    @WithTransaction
    public Uni<Response> updateUser(UpdateUserRequest request) {
        if (securityIdentity == null || securityIdentity.isAnonymous() || jwt == null) {
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED).entity("Token necessário").build());
        }

        Object idClaim = jwt.getClaim("id");
        if (idClaim == null) {
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED).entity("Erro no Token").build());
        }

        Long tokenId;
        if (idClaim instanceof Number) {
            tokenId = ((Number) idClaim).longValue();
        } else {
            try {
                tokenId = Long.parseLong(idClaim.toString());
            } catch (NumberFormatException e) {
                return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).entity("Claim id inválido").build());
            }
        }

        return User.<User>findById(tokenId)
            .onItem().ifNotNull().transformToUni(user -> {
                user.setName(request.name);
                user.setEmail(request.email);
                user.setPassword(hashPassword(request.password));
                return user.persistAndFlush()
                    .onItem().transform(updated -> Response.ok(updated).build());
            })
            .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).entity("Usuário não encontrado").build());
    }

    @POST
    @Path("/operationsBalance")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    @RolesAllowed({"user"})
    public Uni<Response> operationsBalance(UpdateBalanceRequest request) {
        if (securityIdentity == null || securityIdentity.isAnonymous() || jwt == null) {
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED).entity("Token necessário").build());
        }

        Object idClaim = jwt.getClaim("id");
        if (idClaim == null) {
            return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED).entity("Erro no Token").build());
        }
        Long tokenId;
        if (idClaim instanceof Number) {
            tokenId = ((Number) idClaim).longValue();
        } else {
            try {
                tokenId = Long.parseLong(idClaim.toString());
            } catch (NumberFormatException e) {
                return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).entity("Claim id inválido").build());
            }
        }

        return User.<User>findById(tokenId)
            .onItem().ifNotNull().transformToUni(user -> {
                if (request.value != null) {
                    if (request.isAddition != null && request.isAddition == true) {
                        user.updateBalance(request.value);
                    } else if (request.isAddition != null && request.isAddition == false) {
                        user.deductBalance(request.value);
                    }else {
                        return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).entity("campo isAddition(é adição) deve ser true ou false").build());
                    }
                }
                return user.persistAndFlush()
                    .onItem().transform(updated -> Response.ok(updated).build());
            })
            .onItem().ifNull().continueWith(Response.status(Response.Status.NOT_FOUND).entity("Usuário não encontrado").build());
    }

    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    public Uni<User> deleteUser(DeleteUserRequest request) {
        return User.<User>findById(request.id)
            .onItem().ifNotNull()
            .call(item -> {
                return item.delete();
            });
    }

    public static class LoginRequest {
        public String email;
        public String password;
    }

    public static class CreateUserRequest {
        public String name;
        public String email;
        public String password;
        public Double balance;
    }

    public static class UpdateUserRequest {
        public String name;
        public String email;
        public String password;
    }

    public static class DeleteUserRequest {
        public Long id;
    }

    public static class UpdateBalanceRequest {
        public Double value;
        public Boolean isAddition;
    }


}
