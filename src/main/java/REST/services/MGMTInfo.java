package REST.services;

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
        //return list of the drones in the smart city
        //todo
        return Response.ok().build();
    }

    @GET
    @Path(Uri.AdminServer.InfoService.LAST_STATS+"{n}")
    @Produces({"application/json, application/xml"})
    public Response getLastStats(@PathParam("n") int n){
        //return last n info with timestamps
        //todo
        return Response.ok().build();
    }

    @GET
    @Path(Uri.AdminServer.InfoService.AVG_DEL+"{t1}/{t2}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAvgDelivery(@PathParam("t1") int t1, @PathParam("t2") int t2){
        //return average delivery of all drones in smart city timestamp t1 and t2
        //todo
        return Response.ok().build();
    }

    @GET
    @Path(Uri.AdminServer.InfoService.AVG_KM+"{t1}/{t2}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAvgKm(@PathParam("t1") int t1, @PathParam("t2") int t2){
        //return average kilometers of all drone in smart city between timestamp t1 and t2
        //todo
        //debug
        String res = Float.toString((float) (t1+t2)/2);

        return Response.ok(res).build();
    }




}
