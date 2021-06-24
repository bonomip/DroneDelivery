package GRPC.drone.data;

import SENSOR.Measurement;

import java.util.List;

public class LocalStats {

    /*
        a = current avg
        n = number of occurencies
        x = occurence to add
     */
    private static float streamAvg(float a, int n, float x){
        float r = 1.0f/(n);
        return a*(1.0f-r) + x*r;
    }

    public int getMeters() {
        return this.metres;
    }

    public void addMeters(int metres) {
        this.metres += metres;
    }

    public int getDeliveries() {
        return this.deliveries;
    }

    public void addDelivery() {
        this.deliveries += 1;
    }

    public int[] getPosition() {
        return this.position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public int getBattery() { return this.battery; }

    public void setBattery(int battery){ this.battery = battery; }

    public void useBattery(int battery) {
        this.battery -= battery;
    }

    public double getPm10() { return this.pm10; }

    public void setPm10(double p){
        this.pm10 = (double) streamAvg((float)this.pm10, ++this.pm10_count, (float) p);
    }

    public void setPm10_avg(List<Measurement> list){
        this.pm10_avg = list;
    }

    public List<Measurement> getPm10_avg() {
        return this.pm10_avg;
    }

    public void setTime(long time){
        this.time = time;
    }

    public long getTime(){
        return this.time;
    }

    private int metres;

    private int deliveries;

    private int[] position;

    private int battery;

    private double pm10;

    private int pm10_count;

    private long time;

    private List<Measurement> pm10_avg;

    public LocalStats(){
        this.battery = 100;
    }

}
