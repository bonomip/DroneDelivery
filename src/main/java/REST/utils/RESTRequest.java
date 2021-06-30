package REST.utils;

import REST.beans.drone.Drone;
import REST.beans.statistic.Statistic;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

import javax.ws.rs.core.MediaType;
import java.net.ConnectException;

public class RESTRequest {

    public static ClientResponse getRequest(Client client, String url, String type){
        ClientResponse response = client.resource(url)
                .type(type)
                .get(ClientResponse.class);

        if(response.getStatus() != 200)
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());

        return response;
    }

    public static ClientResponse deleteRequest(Client client, String url, Drone drone){
        ClientResponse response = client.resource(url)
                .type(MediaType.APPLICATION_JSON)
                .delete(ClientResponse.class, drone);

        if(response.getStatus() != 200)
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());

        return response;
    }

    public static ClientResponse postRequest(Client client, String url, Drone drone){
        ClientResponse response = client.resource(url)
                    .type(MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class, drone);

        if(response.getStatus() != 200)
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());

        return response;
    }

    public static ClientResponse postRequest(Client client, String url, Statistic stat){
        ClientResponse response = client.resource(url)
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, stat);

        if(response.getStatus() != 200)
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());

        return response;
    }
}
