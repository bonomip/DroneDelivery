package REST.beans.statistic;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Statistic {

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

    public void setTime(long time) {
        this.time = time;
    }

    public void setDeliveries(double deliveries) {
        this.deliveries = deliveries;
    }

    public void setMetres(int metres) {
        this.metres = metres;
    }

    public void setPm10(double pm10) {
        this.pm10 = pm10;
    }

    public void setBattery(double battery) {
        this.battery = battery;
    }

    private long time;
    private double deliveries;
    private int metres;
    private double pm10;
    private double battery;

    public Statistic(){}

    public Statistic(double avg_del, int avg_meters, double avg_pm10, double avg_btry){
        this.time = System.currentTimeMillis();
        this.deliveries = avg_del;
        this.metres = avg_meters;
        this.pm10 = avg_pm10;
        this.battery = avg_btry;
    }

    @Override
    public String toString() {
        return "GlobalStat{" +
                "time=" + time +
                ", deliveries=" + deliveries +
                ", metres=" + metres +
                ", pm10=" + pm10 +
                ", battery=" + battery +
                '}';
    }
}
