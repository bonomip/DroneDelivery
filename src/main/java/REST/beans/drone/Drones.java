package REST.beans.drone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Drones {

    private final ArrayList<Drone> drones;

    public Drones(){
        this.drones = new ArrayList<>();
    }

    public Drones(Drones copy){
        this.drones = new ArrayList<>(copy.getList());
    }

    public ArrayList<Drone> getList() {
        return this.drones;
    }

    public boolean add(Drone drone){
        for(Drone d : this.drones)
            if(d.getId() == drone.getId())
            return false;

        return this.drones.add(drone);
    }

    public boolean remove(Drone drone){
        for(Drone d : this.drones)
            if(d.getId() == drone.getId())
                return this.drones.remove(d);

        return false;
    }

    @Override
    public String toString() {
        return "Drones{" +
                "drones=" + drones +
                '}';
    }
}
