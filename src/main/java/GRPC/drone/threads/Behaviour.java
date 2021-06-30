package GRPC.drone.threads;

import GRPC.drone.Peer;
import REST.beans.drone.Drone;

public abstract class Behaviour extends Thread{

    protected boolean exit = false;

    public void quit(){
        this.exit = true;
    }

    public void printStatus(){
        System.out.println("-------- MY FRIENDS ARE:");
        Peer.MY_FRIENDS.print();
        System.out.println("---- STAT");
        System.out.println("Deliveries: "+Peer.MY_DELIVERIES);
        System.out.println("Metres: "+Peer.MY_METRES);
        System.out.println("Battery: "+Peer.MY_BATTERY);
    };
}
