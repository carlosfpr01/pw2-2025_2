package dev.ifrs;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.eclipse.microprofile.jwt.Claims;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dev.ifrs.model.User;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UsersResource {

    private static final String ISSUER = "users-issuer";
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    // Método auxiliar para hash de senha
    private String hashPassword(String password) {
        return encoder.encode(password);
    }

    // Método auxiliar para verificar senha
    private boolean verifyPassword(String password, String hash) {
        return encoder.matches(password, hash);
    }

    @POST
    @Path("/jwt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @WithTransaction
    public Uni<Response> getToken(LoginRequest request) {
        return User.<User>find("email", request.email).firstResult()
            .onItem().transform(user -> {
                if (user != null && verifyPassword(request.password, user.getPassword())) {
                    String token = Jwt.issuer(ISSUER)
                        .upn(user.getEmail())
                        .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                        .claim(Claims.nickname, user.getName())
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
        return user.persistAndFlush();
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    public Uni<List<User>> listUser() {
        return User.findAll().list();
    }


    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @WithTransaction
    public Uni<User> updateUser(UpdateUserRequest request) {
        return User.<User>findById(request.id)
            .onItem().ifNotNull()
            .call(item -> {
                item.setName(request.name);
                item.setEmail(request.email);
                if (request.password != null && !request.password.isEmpty()) {
                    item.setPassword(hashPassword(request.password));
                }
                return item.persistAndFlush();
            });
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
    }

    public static class UpdateUserRequest {
        public Long id;
        public String name;
        public String email;
        public String password; // Opcional
    }

    public static class DeleteUserRequest {
        public Long id;
    }


}
