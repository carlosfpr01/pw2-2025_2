package run.gastos;

import java.time.LocalDate;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;

import run.gastos.model.Despesa;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/gastos")
public class GastosResource {

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/despesa")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    @WithTransaction
    public Uni<Despesa> createDespesa(CreateDespesaRequest request) {
        /* Validação do token JWT se o token está nulo
        if (securityIdentity == null || securityIdentity.isAnonymous() || jwt == null) {
            return Uni.createFrom().failure(new WebApplicationException("Token Vazio", Response.Status.UNAUTHORIZED));
        }

        //Validar se o campo id existe no token
        Object idClaim = jwt.getClaim("id");
        if (idClaim == null) {
            return Uni.createFrom().failure(new WebApplicationException("Token inválido", Response.Status.UNAUTHORIZED));
        }

        // Criação do id Long baseado no token JWT (forma simples e resiliente)
        Long tokenId;
        try {
            tokenId = Long.parseLong(idClaim.toString());
        } catch (Exception e) {
            return Uni.createFrom().failure(new WebApplicationException("Token inválido", Response.Status.BAD_REQUEST));
        }

        // Garantir que amount seja Double para compatibilidade com Despesa
        Double amount = request.amount == null ? 0.0 : request.amount.doubleValue();

        if (request.operation == null || request.operation.trim().isEmpty()) {
            return Uni.createFrom().failure(new WebApplicationException("Campo 'operation' é obrigatório", Response.Status.BAD_REQUEST));
        }

        Despesa.operations op;
        try {
            op = Despesa.operations.valueOf(request.operation.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return Uni.createFrom().failure(new WebApplicationException("Campo 'operation' inválido (use 'D' ou 'C')", Response.Status.BAD_REQUEST));
        }

        if (request.date == null) {
            return Uni.createFrom().failure(new WebApplicationException("Campo 'date' é obrigatório", Response.Status.BAD_REQUEST));
        }
        */
        Despesa despesa = new Despesa();
        despesa.setIdUser(request.idUser);
        despesa.setAmount(request.amount);
        despesa.setOperation(Despesa.operations.valueOf(request.operation.trim().toUpperCase()));
        despesa.setDate(request.date);
        despesa.setTag(request.tag == null || request.tag.trim().isEmpty() ? "Outros" : request.tag);
        return despesa.persistAndFlush();
    }


    public static class CreateTagRequest {
        public String idUser;
        public String name;
    }

    public static class CreateDespesaRequest {
        public Long idUser;
        public Number amount;
        public String operation;
        public String tag;
        public LocalDate date;
    }

}
