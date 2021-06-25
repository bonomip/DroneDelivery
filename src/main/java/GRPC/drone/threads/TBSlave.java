package GRPC.drone.threads;

import GRPC.drone.Peer;
import REST.beans.drone.Drone;

public class TBSlave extends Behaviour {
    @Override
    public void run() {

        while(!this.exit){
            try {
                //todo remove sleep
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //exit procedures
    }

    @Override
    public void printStatus(){
        System.out.println("---- I'M A SLAVE "+ Peer.ME.getId());
        System.out.println("-------- MY MASTER IS:");
        System.out.println(Peer.MASTER.toString());
        super.printStatus();
    }
}
