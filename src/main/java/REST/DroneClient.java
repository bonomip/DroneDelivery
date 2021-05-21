package REST;

import GRPC.drones.DroneData;
import REST.beans.Drone;
import REST.beans.response.AddDroneResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import utils.Request;
import utils.Uri;

import javax.ws.rs.core.MediaType;

public class DroneClient {


    Client client;

    public DroneClient(){
        this.client = Client.create();
    }

    public DroneData addDroneToNetwork(int id, int port) {
            AddDroneResponse serverResponse = postDrone(this.client, new Drone(id, "localhost", port));
            DroneData data = new DroneData(id, port);
            for(Drone bean : serverResponse.getDrones().getList())
                data.addKnownDrone(bean.getId(), bean.getPort());
            return data;
    }

    public void removeDroneFromNetwork(DroneData drone){

    }

    public static AddDroneResponse postDrone(Client client, Drone drone){
        return Request.postRequest(client, Uri.AdminServer.DroneService.postDrone(), drone )
                .getEntity(AddDroneResponse.class);
    }

    public static void removeDrone(Client client, Drone drone){
        Request.deleteRequest(client, Uri.AdminServer.DroneService.postDrone(), drone);
    }


}
