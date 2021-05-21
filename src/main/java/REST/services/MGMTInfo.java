package REST.services;

import REST.beans.SmartCity;
import utils.Uri;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(Uri.AdminServer.InfoService.INFO_SERVICE)
public class MGMTInfo {

    @GET
    @Path(Uri.AdminServer.InfoService.DRONES)
    @Produces({"application/json, application/xml"})
    public Response getDrones(){
        return Response.ok(SmartCity.getInstance().getDroneList()).build();
    }

    @GET
    @Path(Uri.AdminServer.InfoService.LAST_STATS+"{n}")
    @Produces({"application/json, application/xml"})
    public Response getLastStats(@PathParam("n") int n){
        return Response.ok(SmartCity.getInstance().getLastStatistics(n)).build();
    }

    @GET
    @Path(Uri.AdminServer.InfoService.AVG_DEL+"{t1}/{t2}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvgDelivery(@PathParam("t1") int t1, @PathParam("t2") int t2){
        return Response.ok(SmartCity.getInstance().getAvgDeliveries(t1, t2)).build();
    }

    @GET
    @Path(Uri.AdminServer.InfoService.AVG_KM+"{t1}/{t2}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvgKm(@PathParam("t1") int t1, @PathParam("t2") int t2){
        //todo this method only return an integer not a float
        return Response.ok(SmartCity.getInstance().getAvgKm(t1, t2)).build();
    }




}
