package GRPC.drone.data;

import GRPC.drone.data.stat.DeliveryStat;
import REST.beans.drone.Drone;
import SENSOR.Measurement;
import drone.grpc.deliveryservice.DeliveryService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Slave {

    private Drone drone;
    private boolean delivering;

    private int[] position;
    private int battery;
    private int n_deliveries;

    public ArrayList<DeliveryStat> d_stats;

    public Slave(Drone drone, int[] position, int battery){
        this.drone = drone;

        this.delivering = false;

        this.position = position;
        this.battery = battery;
        this.n_deliveries = 0;

        this.d_stats = new ArrayList<>();
    }

    public synchronized boolean isDelivering(){
        return this.delivering;
    }

    public String getIp(){ return this.drone.getIp(); }

    public int getPort(){ return this.drone.getPort(); }

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
            this.d_stats.add(new DeliveryStat(value));

            this.position = this.d_stats.get(this.d_stats.size()-1).getPosition();
            this.battery = this.d_stats.get(this.d_stats.size()-1).getBattery();
            this.n_deliveries = this.d_stats.size();
            this.delivering = false;

    }

    public synchronized ArrayList<DeliveryStat> getSlaveDeliveryStats(){
        ArrayList<DeliveryStat> tmp = new ArrayList<>(this.d_stats);
        this.d_stats.clear();
        return tmp;
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
