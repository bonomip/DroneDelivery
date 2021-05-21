package REST;

import REST.beans.Drone;
import REST.beans.Drones;
import REST.beans.Statistics;
import REST.beans.response.AddDroneResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import utils.Request;
import utils.Uri;

import javax.ws.rs.core.MediaType;

public class AdminClient {

    public static void main(String[] args) {
        Client client = Client.create();

        //TODO CLI
    }

    public static String getAvgKm(Client client, int t1, int t2){
        return Request.getRequest(client,Uri.AdminServer.InfoService.getAvgKm(t1, t2), MediaType.APPLICATION_JSON)
                .getEntity(String.class);
    }

    public static String getAvgDeliveries(Client client, int t1, int t2){
        return Request.getRequest(client, Uri.AdminServer.InfoService.getAvgDel(t1,t2), MediaType.APPLICATION_JSON)
                .getEntity(String.class);
    }

    public static Drones getDroneList(Client client){
        return Request.getRequest(client, Uri.AdminServer.InfoService.getDrones(), MediaType.APPLICATION_JSON)
                .getEntity(Drones.class);
    }

    public static Statistics getLastStats(Client client, int n){
        return Request.getRequest(client, Uri.AdminServer.InfoService.getLastStats(n), MediaType.APPLICATION_JSON)
                .getEntity(Statistics.class);
    }

}
