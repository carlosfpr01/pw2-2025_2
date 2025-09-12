package dev.ifrs;

import java.util.List;

import dev.ifrs.data.DataBase;
import dev.ifrs.model.Book;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/books")
public class BookResource {

    @Inject
    private DataBase dataBase;


    @GET
    @Path("/list")
    @RolesAllowed("User")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> hello() {
        return dataBase.getBooks();
    }
}
