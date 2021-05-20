package REST;

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
    }


    public static int getAvgKm(Client client, int t1, int t2){
        WebResource resource = client.resource(Uri.AdminServer.InfoService.getAvgKm(t1, t2));
        ClientResponse response = resource.accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);

        if(response.getStatus() != 200)
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());

        return response.getEntity(Integer.class);
    }

    public static int getAvgDelivery(Client client, int t1, int t2){
        WebResource resource = client.resource(Uri.AdminServer.InfoService.getAvgDel(t1, t2));
        ClientResponse response = resource.accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);

        if(response.getStatus() != 200)
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());

        return response.getEntity(Integer.class);
    }

    public static Statistics getLastStats(Client client, int n){
            WebResource resource = client.resource(Uri.AdminServer.InfoService.getLastStats(n));
            ClientResponse response = resource
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .get(ClientResponse.class);

        if(response.getStatus() != 200){
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        return response.getEntity(Statistics.class);
    }
}
