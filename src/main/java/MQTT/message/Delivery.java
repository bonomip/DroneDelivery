package MQTT.message;

import com.google.gson.Gson;

import java.util.Arrays;

public class Delivery {


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getOrigin() {
        return origin;
    }

    public void setOrigin(int[] origin) {
        this.origin = origin;
    }

    public int[] getDestination() {
        return destination;
    }

    public void setDestination(int[] destination) {
        this.destination = destination;
    }

    public boolean isOnProcessing() {
        return onProcessing;
    }

    public void setOnProcessing(boolean b) {
        this.onProcessing = b;
    }

    private int id;
    private int[] origin, destination;

    private boolean onProcessing;

    public Delivery(int id, int[] origin, int[] destination){
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.onProcessing = false;
    }

    public String toJSON(){
        return new Gson().toJson(this);
    }

    public static Delivery fromJSON(String json){
        return new Gson().fromJson(json, Delivery.class);
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", origin=" + Arrays.toString(origin) +
                ", destination=" + Arrays.toString(destination) +
                '}';
    }
}
