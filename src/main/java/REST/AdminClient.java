package REST;

import REST.beans.Drone;
import REST.beans.Drones;
import REST.beans.Statistics;
import REST.beans.response.AddDroneResponse;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import utils.Uri;

import javax.ws.rs.core.MediaType;

public class AdminClient {

    public static void main(String[] args) {
        Client client = Client.create();

        //TODO CLI
    }

    //REST METHODS


    public static String getAvgKm(Client client, int t1, int t2){
        return getRequest(client,Uri.AdminServer.InfoService.getAvgKm(t1, t2), MediaType.APPLICATION_JSON)
                .getEntity(String.class);
    }

    public static String getAvgDeliveries(Client client, int t1, int t2){
        return getRequest(client, Uri.AdminServer.InfoService.getAvgDel(t1,t2), MediaType.APPLICATION_JSON)
                .getEntity(String.class);
    }

    public static Drones getDroneList(Client client){
        return getRequest(client, Uri.AdminServer.InfoService.getDrones(), MediaType.APPLICATION_JSON)
                .getEntity(Drones.class);
    }

    public static Statistics getLastStats(Client client, int n){
        return getRequest(client, Uri.AdminServer.InfoService.getLastStats(n), MediaType.APPLICATION_JSON)
                .getEntity(Statistics.class);
    }

    public static ClientResponse getRequest(Client client, String url, String type){
        ClientResponse response = client.resource(url)
                .type(type)
                .get(ClientResponse.class);

        if(response.getStatus() != 200)
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());

        return response;
    }

}
