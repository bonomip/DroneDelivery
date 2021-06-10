package GRPC.drones.service;

import GRPC.drones.Peer;
import GRPC.drones.data.Slave;
import GRPC.drones.imp.DeliveryImpl;
import MQTT.DeliverySubscriber;
import MQTT.message.Delivery;
import drone.grpc.deliveryservice.DeliverGrpc;
import drone.grpc.deliveryservice.DeliveryService;
import drone.grpc.greeterservice.GreeterGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;


public class Deliver {

    private static void onNext(DeliverySubscriber ds, Slave slave, Delivery delivery, DeliveryService.DeliveryResponse value){
        //todo get stats and save it in ad hoc structure
        System.out.println("[ RESPONSE ] delivery done by drone id "
                + slave.drone.getId()+" @ "+delivery.getId());
        slave.setDelivering(false);
        ds.popDelivery(delivery);


    }

    private static void onError(Slave slave, Delivery delivery){
        System.out.println("[ ERROR ] delivery with drone id "+ slave.drone.getId()+ " @ "+delivery.getId());
        slave.setDelivering(false); //technically useless
        Slave.removeIdFromList(Peer.MY_SLAVES, slave.drone.getId());
        Peer.MY_FRIENDS.removeWithId(slave.drone.getId());
        delivery.setOnProcessing(false);
    }

    private static void onCompleted(Slave slave, ManagedChannel channel){
        //System.out.println("[ CONNECTION ] end connection with drone id "+slave.drone.getId());
        channel.shutdown();
    }

    private static void sendDelivertRequestTo(DeliverySubscriber ds, Slave courier, Delivery delivery) {
        DeliveryService.DeliveryRequest request = DeliveryImpl
                .createDeliveryRequest(delivery.getId(), delivery.getOrigin(), delivery.getDestination());

        System.out.println("-----------------------------");
        System.out.println( "[ DELIVERY ] @ "+delivery.getId());
        System.out.println("[ CONNECTION ] creating channel with drone id "+courier.drone.getId());
        System.out.println("-----------------------------");

        //todo if courier.dorne.getId() == Peer.ME.getId()
        // create a thread that simulate the delivery
        // without sending messages and creating channel


        final ManagedChannel channel = ManagedChannelBuilder
                .forTarget(courier.drone.getIp() + ":" + courier.drone.getPort())
                .usePlaintext()
                .build();

        DeliverGrpc.DeliverStub stub = DeliverGrpc.newStub(channel);

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
                Deliver.onCompleted(courier, channel);
            }
        });
    }

    public static double distance(int[] p1, int[] p2){
        return Math.sqrt( Math.pow(p2[0]-p1[0] , 2) + Math.pow(p2[1]-p1[1], 2));
    }

    private static Slave findCourier(int[] origin){
        if(Peer.MY_SLAVES.size() == 0)
            return null;

        Slave courier = null;
        double lowest_distance = 1000;

        for(Slave s : Peer.MY_SLAVES){

            double distance = distance(s.position, origin);

            if(distance < lowest_distance && !s.delivering)
            {
                lowest_distance = distance;
                courier = s;
            }
        }

        return courier;
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