package GRPC.drone.threads;

import GRPC.drone.Peer;
import GRPC.drone.client.Deliver;
import MQTT.subscriber.DeliverySubscriber;
import MQTT.message.Delivery;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TBMaster extends Behaviour {

    public static final Object DELIVERY_LOCK = new Object();

    DeliverySubscriber deliverySubscriber;

    public TBMaster(){
        this.deliverySubscriber = new DeliverySubscriber();
    }

    public boolean areDeliveriesPending(){
        return this.deliverySubscriber.queueIsEmpty();
    }

    @Override
    public void run() {

        Runnable sendInfo = () -> Peer.REST_CLIENT.sendInfo(Peer.MY_SLAVES.getGlobalStatistic());
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(sendInfo, 5, 10, TimeUnit.SECONDS);

        Runnable showInfo = this::printStatus;
        ScheduledExecutorService executor2 = Executors.newScheduledThreadPool(1);
        executor2.scheduleAtFixedRate(showInfo, 10, 10, TimeUnit.SECONDS);

        while(!this.exit) {
            if(this.areDeliveriesPending())
            {
                Delivery delivery = this.deliverySubscriber.HeadDelivery();
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
            System.out.println("\t\t[ QUIT ] disconnect from mqtt");
            this.deliverySubscriber.closeConnection();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        while(this.areDeliveriesPending() && Peer.MY_SLAVES.size() > 0){
            System.out.println("\t\t[ QUIT ] assign pending deliveries");
            Delivery delivery = this.deliverySubscriber.HeadDelivery();
            delivery.setOnProcessing(true);
            Deliver.assignDelivery(this.deliverySubscriber, delivery);
        }

        synchronized (Deliver.DELIVERY_COUNTER_LOCK){
            while(Deliver.ON_GOING_DELIVERY != 0) {
                System.out.println("\t\t[ QUIT ] wait for deliveries to end");
                try {
                    Deliver.DELIVERY_COUNTER_LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        executor.shutdown();
        executor2.shutdown();
    }

    @Override
    public void printStatus() {
        System.out.println("\n------------------------");
        System.out.println("---- I'M THE MASTER "+Peer.DATA.getMe().getId());
        System.out.println("-------- MY SLAVES ARE:");
        Peer.MY_SLAVES.print();
        super.printStatus();
    }
}
