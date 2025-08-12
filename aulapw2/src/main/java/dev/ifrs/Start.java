package dev.ifrs;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/Conversion")
public class Start {

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

}
