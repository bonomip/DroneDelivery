package REST;

import REST.beans.drone.Drones;
import REST.beans.statistic.Statistics;
import REST.utils.RESTRequest;
import com.sun.jersey.api.client.Client;
import REST.utils.Uri;

import javax.ws.rs.core.MediaType;

public class AdminClient {

    public static void main(String[] args) {
        Client client = Client.create();

        //TODO CLI
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

}
