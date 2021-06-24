package GRPC.drone.data;

public class GlobalStat {

    public long getTime() {
        return time;
    }

    public double getDeliveries() {
        return deliveries;
    }

    public int getMetres() {
        return metres;
    }

    public double getPm10() {
        return pm10;
    }

    public double getBattery() {
        return battery;
    }

    private final long time;
    private final double deliveries;
    private final int metres;
    private final double pm10;
    private final double battery;

    public GlobalStat(double avg_del, int avg_meters, double avg_pm10, double avg_btry){
        this.time = System.currentTimeMillis();
        this.deliveries = avg_del;
        this.metres = avg_meters;
        this.pm10 = avg_pm10;
        this.battery = avg_btry;
    }


}