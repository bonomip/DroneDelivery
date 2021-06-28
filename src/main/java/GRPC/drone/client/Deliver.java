package GRPC.drone.client;

import GRPC.drone.Peer;
import GRPC.drone.data.Slave;
import GRPC.drone.server.DeliveryImpl;
import MQTT.DeliverySubscriber;
import MQTT.message.Delivery;
import drone.grpc.deliveryservice.DeliverGrpc;
import drone.grpc.deliveryservice.DeliveryService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.*;

public class Deliver { /// MASTER DRONE

    public static int ON_GOING_DELIVERY = 0;
    public static final Object DELIVERY_COUNTER_LOCK = new Object();

    private static void onNext(DeliverySubscriber ds, Slave slave, Delivery delivery, DeliveryService.DeliveryResponse value){
        ds.popDelivery(delivery);
        if(value.getBatteryLevel() < 15)
                Peer.MY_SLAVES.removeIdFromList(slave.getId());
        slave.onDeliveryTerminated(value);
    }

    private static void onError(Slave slave, Delivery delivery){
        System.out.println("\t[ DELIVERY ] "+delivery.getId()+" [ ERROR ] by "+ slave.getId());
        slave.setDelivering(false);

        Peer.MY_SLAVES.removeIdFromList(slave.getId());

        if(slave.getId() != Peer.ME.getId())
            Peer.MY_FRIENDS.removeWithId(slave.getId());

        delivery.setOnProcessing(false);

        synchronized (DELIVERY_COUNTER_LOCK){
            ON_GOING_DELIVERY--;
            DELIVERY_COUNTER_LOCK.notify();
        }
    }

    private static void onCompleted(Slave slave, ManagedChannel channel, int del_id){
        System.out.println("\t[ DELIVERY ] "+del_id+" [ COMPLETED ] by "+slave.getId());

        channel.shutdown();

        synchronized (DELIVERY_COUNTER_LOCK){
            ON_GOING_DELIVERY--;
            DELIVERY_COUNTER_LOCK.notify();
        }
    }


    private static void sendDelivertRequestTo(DeliverySubscriber ds, Slave courier, Delivery delivery) {
        DeliveryService.DeliveryRequest request = DeliveryImpl
                .createDeliveryRequest(delivery.getId(), delivery.getOrigin(), delivery.getDestination());

        System.out.println( "\t[ DELIVERY ] "+delivery.getId()+" [ ASSIGNED ] to "+courier.getId());

        //todo if courier.dorne.getId() == Peer.ME.getId()
        // create a thread that simulate the delivery
        // without sending messages and creating channel


        final ManagedChannel channel = ManagedChannelBuilder
                .forTarget(courier.getIp() + ":" + courier.getPort())
                .usePlaintext()
                .build();

        DeliverGrpc.DeliverStub stub = DeliverGrpc.newStub(channel);

        synchronized (DELIVERY_COUNTER_LOCK){
            ON_GOING_DELIVERY++;
        }

        stub.assign(request, new StreamObserver<DeliveryService.DeliveryResponse>() {
            @Override
            public void onNext(DeliveryService.DeliveryResponse value) {
                Deliver.onNext(ds, courier, delivery, value);
            }

            @Override
            public void onError(Throwable t) {
                Deliver.onError(courier, delivery);
            }

            @Override
            public void onCompleted() {
                Deliver.onCompleted(courier, channel, delivery.getId());
            }
        });
    }

    public static double distance(int[] p1, int[] p2){
        return Math.sqrt( Math.pow(p2[0]-p1[0] , 2) + Math.pow(p2[1]-p1[1], 2));
    }

    private static Slave findCourier(int[] origin){
        if(Peer.MY_SLAVES.size() == 0)
            return null;

        List<Integer> courier_ids = Peer.MY_SLAVES.getSlaveNotInDelivery();

        if(courier_ids.size() == 0) return null;

        if(courier_ids.size() == 1) return Peer.MY_SLAVES.getDroneWithId(courier_ids.get(0));

        courier_ids = Peer.MY_SLAVES.getNearestSlave(courier_ids, origin);

        if(courier_ids.size() == 0) return null;

        if(courier_ids.size() == 1) return Peer.MY_SLAVES.getDroneWithId(courier_ids.get(0));

        courier_ids = Peer.MY_SLAVES.getMostCharged(courier_ids);

        if(courier_ids.size() == 0) return null;

        if(courier_ids.size() == 1) return Peer.MY_SLAVES.getDroneWithId(courier_ids.get(0));

        courier_ids.sort(Collections.reverseOrder());

        return Peer.MY_SLAVES.getDroneWithId(courier_ids.get(0));
    }

    public synchronized static void assignDelivery(DeliverySubscriber ds, Delivery delivery){
            Slave courier = findCourier(delivery.getOrigin());

            if(courier == null) {
                //todo if no courier are available
                // wait for a slave to finish a delivery
                    // can be done using a boolean, when no courier are found set b = false;
                    // when a courier finish the delivery set b = true;
                // wait for my_slave.size() > 0
                    // this is bettere because takes in consideration when no slaves are in list
                    // best approach would be to use the two tech togheter
                System.out.println("[ INFO ] no courier found @ "+delivery.getId());
                delivery.setOnProcessing(false);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                courier.setDelivering(true);
                sendDelivertRequestTo(ds, courier, delivery);
            }

    }


}
