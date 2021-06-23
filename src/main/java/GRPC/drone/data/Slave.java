package GRPC.drone.data;

import REST.beans.drone.Drone;

import java.util.ArrayList;
import java.util.Arrays;

public class Slave {

    public Drone drone;
    public int[] position;
    private boolean delivering;
    private int battery;

    public Slave(Drone drone, int[] position){
        this.drone = drone;
        this.position = position;
        this.delivering = false;
    }

    public boolean isDelivering(){
        return this.delivering;
    }

    public int getId(){
        return this.drone.getId();
    }

    public void setDelivering(boolean b){
        this.delivering = b;
    }

    public void setBattery(int i){ this.battery = i; }

    public int getBattery() { return this.battery; }
    @Override
    public String toString() {
        return "{" +
                "\"id\":" + drone.getId() +
                ", \"position\":" + Arrays.toString(position) +
                ", \"delivering\":" + delivering +
                "}";
    }
}
