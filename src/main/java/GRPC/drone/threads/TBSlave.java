package GRPC.drone.threads;

import GRPC.drone.Peer;
import GRPC.drone.client.Deliver;
import GRPC.drone.client.Greeter;
import GRPC.drone.client.HeartBeat;
import GRPC.drone.server.DeliveryImpl;
import GRPC.drone.server.ElectionImpl;
import REST.beans.drone.Drone;

public class TBSlave extends Behaviour {
    @Override
    public void run() {

        while(!this.exit){
            try {

                synchronized (ElectionImpl.LOCK){
                    if(ElectionImpl.ELECTION)
                        ElectionImpl.LOCK.wait();
                }

                HeartBeat.beat(Peer.DATA.getMaster().getIp(), Peer.DATA.getMaster().getPort());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1500);
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
        System.out.println("---- I'M A SLAVE "+ Peer.DATA.getMe().getId());
        System.out.println("-------- MY MASTER IS:");
        System.out.println(Peer.DATA.getMaster().toString());
        super.printStatus();
    }
}
