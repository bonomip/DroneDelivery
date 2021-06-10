package GRPC.drones.data;

import REST.beans.drone.Drone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static boolean isIdInList(ArrayList<Slave> list, int id){
        return list.stream().anyMatch(s -> s.drone.getId() == id);
    }

    public static void removeIdFromList(ArrayList<Slave> list, int id){
        list.removeIf(s -> s.drone.getId() == id);
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
