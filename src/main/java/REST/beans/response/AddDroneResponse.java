package REST.beans.response;

import REST.beans.Drone;
import REST.beans.Drones;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AddDroneResponse {

    private Drones drones;
    private int[] startPosition;

    public AddDroneResponse(){}

    public AddDroneResponse(Drones drones, int[] position){
        this.drones = drones;
        this.startPosition = position;
    }

    public Drones getDrones() {
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
