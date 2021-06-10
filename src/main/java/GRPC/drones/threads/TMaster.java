package GRPC.drones.threads;

import GRPC.drones.Peer;
import GRPC.drones.data.Slave;
import GRPC.drones.service.Deliver;
import MQTT.DeliverySubscriber;
import MQTT.message.Delivery;
import REST.beans.drone.Drone;

import java.util.Arrays;

public class TMaster extends Behaviour {

    DeliverySubscriber deliverySubscriber;

    //remember : if im master im also encarged with slaves duties
    public TMaster(){
        this.deliverySubscriber = new DeliverySubscriber();
    }


    public boolean areDeliveriesPending(){
        return this.deliverySubscriber.queueIsEmpty();
    }

    @Override
    public void run() {

        while(!this.exit){

            if(this.areDeliveriesPending())
            {
                Delivery delivery = this.deliverySubscriber.HeadDelivery();
                delivery.setOnProcessing(true);
                //todo can be done by a thread
                Deliver.assignDelivery(this.deliverySubscriber, delivery);
            } else
            {
                //System.out.println(" [ INFO ] no pending deliveries");
            }

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
    public void printStatus() {
        System.out.println("---- I'M THE MASTER "+Peer.ME.getId());
        System.out.println("-------- MY SLAVES ARE:");
        for(Slave s : Peer.MY_SLAVES)
            System.out.println(s.toString());
        System.out.println("-------- MY FRIENDS ARE:");
        for(Drone d : Peer.MY_FRIENDS){
            System.out.println(d.toString());
        }
    }
}
