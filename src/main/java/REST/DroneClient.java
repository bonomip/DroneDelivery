package REST;

import REST.beans.drone.Drone;
import REST.beans.response.AddDroneResponse;
import REST.beans.statistic.Statistic;
import REST.utils.RESTRequest;
import com.sun.jersey.api.client.Client;
import REST.utils.Uri;

import java.util.HashMap;

public class DroneClient {


    Client client;

    public DroneClient(){
        this.client = Client.create();
    }

    private static AddDroneResponse postDrone(Client client, Drone drone){
        return RESTRequest.postRequest(client, Uri.AdminServer.DroneService.postDrone(), drone )
                .getEntity(AddDroneResponse.class);
    }

    private static void removeDrone(Client client, Drone drone){
        RESTRequest.deleteRequest(client, Uri.AdminServer.DroneService.deleteDrone(), drone);
    }

    private static void setSmartCityInfo(Client client, Statistic stat){
        RESTRequest.postRequest(client, Uri.AdminServer.DroneService.postInfo(), stat);
    }

    ///////

    public void sendInfo(Statistic stat){
        if(stat == null) return;

        System.out.println("[SENDING INFO]");

        setSmartCityInfo(client, stat);
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



}
