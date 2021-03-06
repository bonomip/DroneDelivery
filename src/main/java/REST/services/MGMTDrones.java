package REST.services;

import REST.beans.drone.Drone;
import REST.beans.response.AddDroneResponse;
import REST.beans.statistic.Statistic;
import REST.beans.SmartCity;
import REST.utils.Uri;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path(Uri.AdminServer.DroneService.DRONE_SERVICE)
public class MGMTDrones {

    @POST
    @Consumes({"application/json, application/xml"})
    public Response addDrone(Drone drone){
        AddDroneResponse response = SmartCity.getInstance().addDrone(drone);
        if(response == null)
            return Response.ok().status(Response.Status.BAD_REQUEST).build();

        return Response.ok(response).build();
    }

    @DELETE
    @Consumes({"application/json, application/xml"})
    public Response removeDrone(Drone drone){
        if(SmartCity.getInstance().removeDrone(drone))
            return Response.ok().build();
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @Path(Uri.AdminServer.DroneService.MASTER)
    @Produces({"application/json, application/xml"})
    public Response setSmartCityInfo(Statistic stat){
        SmartCity.getInstance().addStatistic(stat);
        return Response.ok().build();
    }




}
