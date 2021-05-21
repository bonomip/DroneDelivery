package GRPC.drones;

import java.util.ArrayList;

public class DroneData {

    private int port;
    private int id;

    private ArrayList<DroneData> drones;
    private int[] position;

    public DroneData(int id, int port){
        this.port = port;
        this.id = id;
        this.drones = new ArrayList<>();
    }

    public void addKnownDrone(int port, int id){
        this.drones.add(new DroneData(port, id));
    }

    public void setDrones(ArrayList<DroneData> drones) {
        this.drones = drones;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }
}
