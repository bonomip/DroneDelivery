package GRPC.drone.data;

import GRPC.drone.server.DeliveryImpl;
import REST.beans.drone.Drone;
import drone.grpc.deliveryservice.DeliveryService;

import java.util.ArrayList;
import java.util.Arrays;

public class Slave {

    public Drone drone;
    private boolean delivering;
    private int[] position;
    private int battery;
    public ArrayList<LocalStats> stats;

    public Slave(Drone drone, int[] position){
        this.drone = drone;
        this.delivering = false;
        this.stats = new ArrayList<>();
        this.position = position;
    }

    public synchronized boolean isDelivering(){
        return this.delivering;
    }

    public int getId(){
        return this.drone.getId();
    }

    public synchronized void setDelivering(boolean b){
        this.delivering = b;
    }

    public synchronized int[] getPosition(){
        return this.position;
    }

    public int getBattery(){
        return this.battery;
    }

    public synchronized void onDeliveryTerminated(DeliveryService.DeliveryResponse value){
        LocalStats s = new LocalStats();
        this.delivering = false;
        s.setBattery(value.getBatteryLevel());
        s.addDelivery();
        s.setPosition(new int[] { value.getXPosition(), value.getYPosition() } );
        s.addMeters(value.getMetersDone());
        s.setPm10_avg(DeliveryImpl.unpackPm10(value));
        s.setTime(value.getDeliveryTime());
        this.battery = value.getBatteryLevel();
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + drone.getId() +
                ", \"position\":" + Arrays.toString(this.position) +
                ", \"delivering\":" + delivering +
                "}";
    }
}
