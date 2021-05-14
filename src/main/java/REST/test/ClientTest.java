package REST.test;

import REST.beans.Drone;
import REST.beans.response.AddDroneResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import utils.Uri;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

public class ClientTest {

    public static String getAvgKm(Client client, int t1, int t2){
        WebResource resource = client.resource(Uri.AdminServer.InfoService.getAvgKm(t1, t2));
        ClientResponse response = resource.accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);

        if(response.getStatus() != 200)
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());

        return response.getEntity(String.class);
    }

    public static Drone newRndDrone(int max_id, int max_port){
        return new Drone( (int)(Math.random()*max_id),
                "local:host",
                (int)(Math.random()*max_port));
    }

    // ADD AND REMOVE DRONES

    public static AddDroneResponse postDrone(Client client, Drone drone){
        WebResource resource = client.resource(Uri.AdminServer.DroneService.postDrone());

        System.out.println("[TRY] \t\t Drone ID => "+drone.getId());
        ClientResponse response = resource
                .accept("application/json")
                .type("application/json")
                .post(ClientResponse.class, drone);

        if(response.getStatus() != 200){
            System.out.println("[ERROR] \t\t Drone ID "+drone.getId()+" already exist!");
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus() + " -- " + response.getEntity(String.class));
        }

        System.out.println("[ADD] \t\t Drone ID => "+drone.getId());

        return response.getEntity(AddDroneResponse.class);
    }

    public static void removeDrone(Client client, Drone drone){
        WebResource resource = client.resource(Uri.AdminServer.DroneService.postDrone());

        ClientResponse response = resource
                .accept("application/json")
                .type("application/json")
                .delete(ClientResponse.class, drone);

        if(response.getStatus() != 200)
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        System.out.println("[REMOVE] \t\t Drone ID => "+drone.getId());
    }

    public static void createThreadThatCreatesDrones(Client client, int n){
        //create threads
        Thread[] ts = new Thread[n];
        for(int i = 0; i < n; i++){
            ts[i] = new Thread(() -> {
                try {
                    Thread.sleep((int)(Math.random()*20));
                    AddDroneResponse r = postDrone(client, newRndDrone(20, 2000));
                    System.out.println(r.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        //run threads
        for (Thread t : ts){
            t.start();
        }
    }

    public static void createThreadThatCreatesAndDeleteDrones(Client client, int n){
        //create threads
        Thread[] ts = new Thread[n];
        for(int i = 0; i < n; i++){
            ts[i] = new Thread(() -> {

                try{ Thread.sleep((int)(Math.random()*50));} catch (Exception e){e.printStackTrace();}
                    Drone drone = newRndDrone(20, 2000);
                    AddDroneResponse r = postDrone(client, drone);
                    removeDrone(client, drone);
            });
        }

        //run threads
        for (Thread t : ts){
            t.start();
        }
    }

    public static void main(String[] args) {

        try {
            Client client = Client.create();
            createThreadThatCreatesAndDeleteDrones(client, 40);
        } catch (Exception e){e.printStackTrace();}
    }

}
