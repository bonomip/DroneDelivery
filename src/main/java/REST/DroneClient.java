package REST;

import GRPC.drone.data.stat.GlobalStat;
import REST.beans.drone.Drone;
import REST.beans.response.AddDroneResponse;
import com.sun.jersey.api.client.Client;
import REST.utils.Request;
import REST.utils.Uri;

import java.util.HashMap;

public class DroneClient {


    Client client;

    public DroneClient(){
        this.client = Client.create();
    }

    public void sendInfo(GlobalStat stat){

        System.out.println("[SENDING INFO]");
        System.out.println(stat.toString());

        if(stat == null) return;

        //todo
    }

    public HashMap<String, Object> addDroneToNetwork(int id, String ip, int port) {
        HashMap<String, Object> result = new HashMap<>();

        AddDroneResponse serverResponse = postDrone(this.client, new Drone(id, ip, port));

        result.put("drone", new Drone(id, ip, port));
        result.put("drones", serverResponse.getDrones());
        result.put("position", serverResponse.getStartPosition());

        return result;
    }

    public void removeDroneFromNetwork(Drone drone){
        removeDrone(this.client, drone);
    }

    private static AddDroneResponse postDrone(Client client, Drone drone){
        return Request.postRequest(client, Uri.AdminServer.DroneService.postDrone(), drone )
                .getEntity(AddDroneResponse.class);
    }

    private static void removeDrone(Client client, Drone drone){
        Request.deleteRequest(client, Uri.AdminServer.DroneService.postDrone(), drone);
    }


}
