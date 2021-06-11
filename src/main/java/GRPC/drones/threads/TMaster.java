package GRPC.drones.threads;

import GRPC.drones.Peer;
import GRPC.drones.data.Slave;
import GRPC.drones.service.Deliver;
import MQTT.DeliverySubscriber;
import MQTT.message.Delivery;
import REST.beans.drone.Drone;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Arrays;

public class TMaster extends Behaviour {

    public static final Object DELIVERY_LOCK = new Object();

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
                synchronized (DELIVERY_LOCK){
                    try {
                        DELIVERY_LOCK.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        try {
            this.deliverySubscriber.closeConnection();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        //assign pending deliveries
        //send global stats to

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
