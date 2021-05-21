package REST.beans;

import REST.beans.response.AddDroneResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SmartCity {

    private Drones drones;
    private Statistics stats;

    private static SmartCity instance;

    private SmartCity(){
        this.drones = new Drones();
        this.stats = new Statistics();
    }

    public synchronized static SmartCity getInstance(){
        if(SmartCity.instance == null)
            SmartCity.instance = new SmartCity();
        return SmartCity.instance;
    }

    public synchronized boolean addStatistic(Statistic stat){
        return this.stats.add(stat);
    }

    public Statistics getLastStatistics(int n){
        return this.stats.getLast(n);
    }

    public float getAvgKm(int t1, int t2){
        //todo
        return ((float)(t1+t2))/2;
    }

    public float getAvgDeliveries(int t1, int t2){
        //todo
        return ((float)(t1+t2))/2;
    }

    public synchronized Drones getDroneList(){
        return this.drones;
    }

    public synchronized AddDroneResponse addDrone(Drone drone) {

        //the constructor passes the object as a completly different copy
        if(this.drones.add(drone))
            return new AddDroneResponse(new Drones(this.drones), RNDMPOS());
        return null;
    }

    public synchronized boolean removeDrone(Drone drone){
        return this.drones.remove(drone);
    }

    private static int[] RNDMPOS(){
        //todo
        return new int[] {(int)(Math.random()*10), (int)(Math.random()*10)};
    }

}
