package GRPC.drones.imp;


import GRPC.drones.Peer;
import GRPC.drones.service.Deliver;
import drone.grpc.deliveryservice.DeliverGrpc;
import drone.grpc.deliveryservice.DeliveryService;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.Random;

public class DeliveryImpl extends DeliverGrpc.DeliverImplBase {
    @Override
    public void assign(
            DeliveryService.DeliveryRequest request,
            StreamObserver<DeliveryService.DeliveryResponse> responseObserver) {

        int[] position = new int[] {request.getXDestination(), request.getYDestination()};
        int[] origin = new int[] {request.getXOrigin(), request.getYOrigin()};
        int meters = (int) (Deliver.distance(origin, position) * 1000);
        int id = request.getId();

        System.out.println("[ DELIVERY ] assigned to drone id "+Peer.ME.getId()+" @ "+id);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long time = System.currentTimeMillis();


        //todo 10% di 100 o 10% di Peer.BATTERY ?
        Peer.BATTERY -= 10;

        DeliveryService.DeliveryResponse response =
                createDeliveryResponse(id, time, position, meters, Peer.BATTERY, Math.random()*10, time);

        //todo exit from the network if battery < 15

        System.out.println("[ DELIVERY ] done by drone id "+Peer.ME.getId()+" @ "+id);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public static DeliveryService.DeliveryRequest createDeliveryRequest(
            int id, int[] origin, int[] destination) {
        return DeliveryService.DeliveryRequest.newBuilder()
                .setId(id)
                .setXOrigin(origin[0])
                .setYOrigin(origin[1])
                .setXDestination(destination[0])
                .setYDestination(destination[1])
                .build();
    }

    public static DeliveryService.DeliveryResponse createDeliveryResponse(
            int id, long timeStamp, int[] position, int meters, int battery, double pm10, long pm10TimeStamp ) {
        return DeliveryService.DeliveryResponse.newBuilder()
                .setId(id)
                .setDeliveryTime(timeStamp)
                .setXPosition(position[0])
                .setYPosition(position[1])
                .setMetersDone(meters)
                .setBatteryLevel(battery)
                .setPm10(pm10)
                .setPm10Time(pm10TimeStamp)
                .build();
    }
}