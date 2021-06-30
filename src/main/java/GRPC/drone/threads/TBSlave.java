package GRPC.drone.threads;

import GRPC.drone.Peer;
import GRPC.drone.client.Deliver;
import GRPC.drone.client.Greeter;
import GRPC.drone.client.HeartBeat;
import GRPC.drone.server.DeliveryImpl;
import REST.beans.drone.Drone;

public class TBSlave extends Behaviour {

    @Override
    public void run() {

        while(!this.exit){

            try {
                HeartBeat.beat(Peer.MASTER.getIp(), Peer.MASTER.getPort());
            } catch (InterruptedException e) {
                System.out.println("[ ELECTION ] [ START2 ]");
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        synchronized (DeliveryImpl.DELIVERY_LOCK){
            while(DeliveryImpl.DELIVERING) {
                System.out.println("\t\t[ QUIT ] wait for deliveries to end");
                try {
                    DeliveryImpl.DELIVERY_LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void printStatus(){
        System.out.println("---- I'M A SLAVE "+ Peer.ME.getId());
        System.out.println("-------- MY MASTER IS:");
        System.out.println(Peer.MASTER.toString());
        super.printStatus();
    }
}
