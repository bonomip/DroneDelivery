package GRPC.drones;

import REST.DroneClient;

public class Drone {

    public static int id = 0;
    public static int port = 1000;

    public static DroneData data;
    public static DroneClient client;
    public static DronePeer peer;

    public static void main(String[] args) {
            int id = 0;
            int port = 1000;

            DroneClient client = new DroneClient();
            DroneData data = client.addDroneToNetwork(id, port);
    }

}
