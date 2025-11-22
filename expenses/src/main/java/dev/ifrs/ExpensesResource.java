package dev.ifrs;

import javax.annotation.processing.Generated;

import dev.ifrs.model.Balance;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Consumes;

@Path("/expenses")
public class ExpensesResource {

    @Path("/gastos")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Spend createSpend(Spend spend) {
        spend.persist();
        return spend;
    }

}
