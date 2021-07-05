package GRPC.drone.threads;

import GRPC.drone.Peer;
import GRPC.drone.client.Deliver;
import GRPC.drone.client.Election;
import GRPC.drone.client.Greeter;
import GRPC.drone.client.HeartBeat;
import GRPC.drone.server.DeliveryImpl;
import GRPC.drone.server.ElectionImpl;
import REST.beans.drone.Drone;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TBSlave extends Behaviour {
    @Override
    public void run() {

        Runnable showInfo = () -> Peer.BTHREAD.printStatus();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(showInfo, 10, 10, TimeUnit.SECONDS);


        while(!this.exit){
            try {
                synchronized (Election.FINISH){
                    while(Election.PARTICIPANT) {
                        System.out.println("WAIT TO ELECTION TO FINISH");
                        Election.FINISH.wait();
                        System.out.println("[ELECTION] FINISH ");
                    }
                }

                if(Peer.DATA.getMaster() != null || !Peer.DATA.isMasterDrone())
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

        executor.shutdown();

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

        System.out.println("[SLAVE THREAD FINISHED]");
    }

    @Override
    public void printStatus(){
        System.out.println("\n------------------------");
        System.out.println("---- I'M A SLAVE "+ Peer.DATA.getMe().getId());
        System.out.println("-------- MY MASTER IS:");
        System.out.println(Peer.DATA.getMaster().toString());
        super.printStatus();
    }
}
