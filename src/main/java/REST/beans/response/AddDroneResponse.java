package REST.beans.response;

import REST.beans.Drone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AddDroneResponse {

    private ArrayList<Drone> drones;
    private int[] startPosition;

    public AddDroneResponse(){}

    public AddDroneResponse(ArrayList<Drone> drones, int[] position){
        this.drones = drones;
        this.startPosition = position;
    }

    public ArrayList<Drone> getDrones() {
        return drones;
    }

    public int[] getStartPosition() {
        return startPosition;
    }

    @Override
    public String toString() {
        return "AddDroneResponse{" +
                "drones=" + drones +
                ", startPosition=" + Arrays.toString(startPosition) +
                '}';
    }
}
