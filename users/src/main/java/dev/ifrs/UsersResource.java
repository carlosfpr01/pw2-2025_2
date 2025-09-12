package dev.ifrs;


import java.util.Arrays;
import java.util.HashSet;

import org.eclipse.microprofile.jwt.Claims;

import dev.ifrs.model.User;
import io.smallrye.jwt.build.Jwt;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/users")
public class UsersResource {

    private static final String ISSUER = "users-issuer";

    @POST
    @Path("/jwt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String getToken(User user) {
        return Jwt.issuer(ISSUER)
            .upn(user.getEmail())
            .groups(new HashSet<>(Arrays.asList("User", "Admin")))
            .claim(Claims.nickname, user.getName())
            .sign();
    }

    
}
