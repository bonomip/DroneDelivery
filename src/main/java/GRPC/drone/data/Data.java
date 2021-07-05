package GRPC.drone.data;

import REST.beans.drone.Drone;

public class Data {


    Drone me, master;
    int[] position;
    int battery;
    int deliveries;
    long metres;

    public synchronized int getDeliveries() {
        return deliveries;
    }

    public synchronized void setDeliveries(int deliveries) {
        this.deliveries = deliveries;
    }

    public synchronized long getMetres() {
        return metres;
    }

    public synchronized void setMetres(long metres) {
        this.metres = metres;
    }

    public synchronized  int[] getPosition() {
        return position;
    }

    public synchronized void setPosition(int[] position) {
        this.position = position;
    }

    public synchronized int getBattery() {
        return battery;
    }

    public synchronized int getRelativeBattery(){
        //i'm never on delivery becaouse the master
        //will wait for all deliveries to end before quitting
        //so the election will start when all the drone are idling
        return this.battery;
    }

    public synchronized void setBattery(int battery) {
        this.battery = battery;
    }

    public synchronized void setMe(Drone me){
        this.me = me;
    }

    public synchronized Drone getMe(){
        return this.me;
    }

    public synchronized Drone getMaster(){
        return this.master;
    }

    public synchronized boolean isMasterDrone(){
        return this.master != null && this.master.getId() == this.me.getId();
    }

    public synchronized void setMasterDrone(Drone master){
        this.master = master;
    }

    public Data(){}

}
