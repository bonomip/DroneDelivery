package GRPC.drone.data.stat;

import GRPC.drone.server.DeliveryImpl;
import SENSOR.Measurement;
import drone.grpc.deliveryservice.DeliveryService;

import java.util.List;

public class DeliveryStat  {

    public int[] getPosition() {
        return position;
    }

    public int getBattery() {
        return battery;
    }

    public long getMetres() {
        return metres;
    }

    public long getTime() {
        return time;
    }

    public List<Measurement> getPm10() {
        return this.pm10;
    }

    private final int[] position;
    private final int battery;
    private final long metres;
    private final long time;
    private List<Measurement> pm10;

    public DeliveryStat(DeliveryService.DeliveryResponse value){
        this.position = new int[] { value.getXPosition(), value.getYPosition() };
        this.battery = value.getBatteryLevel();
        this.metres = value.getMetersDone();
        this.pm10 = DeliveryImpl.unpackPm10(value);
        this.time = value.getDeliveryTime();
    }

}
