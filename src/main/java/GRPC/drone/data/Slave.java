package GRPC.drone.data;

import REST.beans.drone.Drone;

import java.util.ArrayList;
import java.util.Arrays;

public class Slave {

    public Drone drone;
    public int[] position;
    public boolean delivering;

    public Slave(Drone drone, int[] position){
        this.drone = drone;
        this.position = position;
        this.delivering = false;
    }

    public void setDelivering(boolean b){
        this.delivering = b;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + drone.getId() +
                ", \"position\":" + Arrays.toString(position) +
                ", \"delivering\":" + delivering +
                "}";
    }
}
