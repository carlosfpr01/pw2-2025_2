package dev.ifrs;

import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/Conversion")
public class Start {

    private List<Tarefa> tarefas = new ArrayList<>();

    @POST
    @Path("/km-to-miles/")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.TEXT_PLAIN)
    public String postKmToMiles(@FormParam("km") String km) {
        Double miles = Double.parseDouble(km) * 0.621371;
        return String.format("%.5f", miles);
    }

    @GET
    @Path("/knots-to-km/{knots}")
    @Produces(MediaType.APPLICATION_JSON)
    public Float convertKnotsToKm(@PathParam("knots") Float knots) {
        return (knots * 1.852f);
    }

    @GET
    @Path("/query")
    @Produces(MediaType.TEXT_PLAIN)
    public String query(
        @QueryParam("name")String name,
        @HeaderParam("Host") String host,
        @HeaderParam("User-Agents") String user
    ){
        return name + " " + host + " " + user;
    }

    @POST
    @Path("/tarefa")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Tarefa tarefa(Tarefa postTarefas) {
        Tarefa t = new Tarefa(postTarefas.getName(), postTarefas.getContent());
        tarefas.add(t);
        return  t;
    }

    @GET
    @Path("/tarefa")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Tarefa> getTarefa(){
        return tarefas;
    }
}
