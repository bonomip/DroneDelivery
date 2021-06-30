package REST.beans.statistic;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Statistic {

    public long getTime() {
        return time;
    }

    public float getDeliveries() {
        return deliveries;
    }

    public long getMetres() {
        return metres;
    }

    public float getPm10() {
        return pm10;
    }

    public float getBattery() {
        return battery;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setDeliveries(float deliveries) {
        this.deliveries = deliveries;
    }

    public void setMetres(long metres) {
        this.metres = metres;
    }

    public void setPm10(float pm10) {
        this.pm10 = pm10;
    }

    public void setBattery(float battery) {
        this.battery = battery;
    }

    private long time;
    private float deliveries;
    private long metres;
    private float pm10;
    private float battery;

    public Statistic(){}

    public Statistic(float avg_del, long avg_meters, float avg_pm10, float avg_btry){
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
