package REST.beans;

import REST.beans.response.AddDroneResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SmartCity {

    private ArrayList<Drone> drones;
    private ArrayList<Info> infos;

    private static SmartCity instance;

    private SmartCity(){
        this.drones = new ArrayList<>();
        this.infos = new ArrayList<>();
    }

    public synchronized static SmartCity getInstance(){
        if(SmartCity.instance == null)
            SmartCity.instance = new SmartCity();
        return SmartCity.instance;
    }

    public synchronized AddDroneResponse addDrone(Drone drone) {

        try{ Thread.sleep((int)(Math.random()*20));} catch (Exception e){e.printStackTrace();}

        for(Drone d : this.drones)
            if(d.getId() == drone.getId())
                return null;

        this.drones.add(drone);

        //the list is passed as a copy to avoid memory conflict
        // note : java passes list as reference
        return new AddDroneResponse(new ArrayList<>(this.drones), RNDMPOS());
    }

    public synchronized boolean removeDrone(Drone drone){

        try{ Thread.sleep((int)(Math.random()*20));} catch (Exception e){e.printStackTrace();}

        for(Drone d : this.drones)
            if(d.getId() == drone.getId())
                return this.drones.remove(d);

        return false;
    }

    private static int[] RNDMPOS(){
        //todo
        return new int[] {(int)(Math.random()*10), (int)(Math.random()*10)};
    }

}
