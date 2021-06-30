package REST.test;

import REST.beans.drone.Drone;
import REST.beans.drone.Drones;
import REST.beans.statistic.Statistics;
import REST.beans.response.AddDroneResponse;
import REST.utils.RESTRequest;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import REST.utils.Uri;

import javax.ws.rs.core.MediaType;

public class ClientTest {

    public static void main(String[] args) {

        try {
            Client client = Client.create();

            System.out.println(getLastStats(client, 10));
            System.out.println(getAvgKm(client, 10, 3));
            System.out.println(getAvgDeliveries(client, 4, 7));

            Drone d1 = newRndDrone(1000, 4000);
            Drone d2 = newRndDrone(1000, 4000);
            Drone d3 = newRndDrone(1000, 4000);
            System.out.println(postDrone(client, d1));
            System.out.println(postDrone(client, d2));
            System.out.println(postDrone(client, d3));


            System.out.println(getDroneList(client));

            removeDrone(client, d1);

            System.out.println(getDroneList(client));

        } catch (Exception e){e.printStackTrace();}
    }

    public static String getAvgKm(Client client, int t1, int t2){
        return RESTRequest.getRequest(client,Uri.AdminServer.InfoService.getAvgKm(t1, t2), MediaType.APPLICATION_JSON)
                .getEntity(String.class);
    }

    public static String getAvgDeliveries(Client client, int t1, int t2){
        return RESTRequest.getRequest(client, Uri.AdminServer.InfoService.getAvgDel(t1,t2), MediaType.APPLICATION_JSON)
                .getEntity(String.class);
    }

    public static Drones getDroneList(Client client){
        return RESTRequest.getRequest(client, Uri.AdminServer.InfoService.getDrones(), MediaType.APPLICATION_JSON)
                .getEntity(Drones.class);
    }

    public static Statistics getLastStats(Client client, int n){
        return RESTRequest.getRequest(client, Uri.AdminServer.InfoService.getLastStats(n), MediaType.APPLICATION_JSON)
                .getEntity(Statistics.class);
    }

    public static AddDroneResponse postDrone(Client client, Drone drone){
        return RESTRequest.postRequest(client, Uri.AdminServer.DroneService.postDrone(), drone )
                .getEntity(AddDroneResponse.class);
    }

    public static void removeDrone(Client client, Drone drone){
        RESTRequest.deleteRequest(client, Uri.AdminServer.DroneService.postDrone(), drone);
    }

    // ADD AND REMOVE DRONES

    public static void removeDrone2(Client client, Drone drone){
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

    public static Drone newRndDrone(int max_id, int max_port){
        return new Drone( (int)(Math.random()*max_id),
                "local:host",
                (int)(Math.random()*max_port));
    }
}
