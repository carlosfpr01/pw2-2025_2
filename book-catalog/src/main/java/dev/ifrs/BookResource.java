package dev.ifrs;

import java.util.List;

import dev.ifrs.data.DataBase;
import dev.ifrs.model.Book;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/books")
public class BookResource {

    @Inject
    DataBase database;

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> listBooks() {
        return database.getBooks();
    }
}
