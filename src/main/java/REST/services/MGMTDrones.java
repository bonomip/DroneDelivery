package REST.services;

import REST.beans.Drone;
import REST.beans.response.AddDroneResponse;
import REST.beans.Info;
import REST.beans.SmartCity;
import utils.Uri;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path(Uri.AdminServer.DroneService.DRONE_SERVICE)
public class MGMTDrones {

    @POST
    @Consumes({"application/json, application/xml"})
    public Response addDrone(Drone drone){
        AddDroneResponse response = SmartCity.getInstance().addDrone(drone);
        if(response == null)
            return Response.ok(drone.getId()).status(Response.Status.CONFLICT).build();

        return Response.ok(response).build();
    }

    @DELETE
    @Consumes({"application/json, application/xml"})
    public Response removeDrone(Drone drone){
        if(SmartCity.getInstance().removeDrone(drone))
            return Response.ok().build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/master")
    @Produces({"application/json, application/xml"})
    public Response setSmartCityInfo(Info info){
        //put the info in ad hoc data structure
        ////it's going to be used for further processing operations
        //todo
        return Response.ok().build();
    }




}
