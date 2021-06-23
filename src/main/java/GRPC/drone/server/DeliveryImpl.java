package GRPC.drone.server;
import GRPC.drone.Peer;
import GRPC.drone.client.Deliver;
import SENSOR.Measurement;
import SENSOR.PM10Buffer;
import drone.grpc.deliveryservice.DeliverGrpc;
import drone.grpc.deliveryservice.DeliveryService;
import io.grpc.stub.StreamObserver;

import java.util.List;


public class DeliveryImpl extends DeliverGrpc.DeliverImplBase {
    @Override
    public void assign(
            DeliveryService.DeliveryRequest request,
            StreamObserver<DeliveryService.DeliveryResponse> responseObserver) {

        System.out.println("[ DELIVERY ] assigned to drone id "+Peer.ME.getId()+" @ "+request.getId());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int[] position = new int[] {request.getXDestination(), request.getYDestination()};
        int[] origin = new int[] {request.getXOrigin(), request.getYOrigin()};
        int meters = (int) (Deliver.distance(origin, position) * 1000);
        int id = request.getId();
        long time = System.currentTimeMillis();
        List<Measurement> pm10 = Peer.SENSOR_BUFFER.readAllAndClean();

        //todo 10% di 100 o 10% di Peer.BATTERY ?
        Peer.BATTERY -= 10;

        DeliveryService.DeliveryResponse response =
                createDeliveryResponse(id, time, position, meters, Peer.BATTERY, pm10);

        System.out.println("[ DELIVERY ] done by drone id "+Peer.ME.getId()+" @ "+id);

        if(Peer.BATTERY < 15)
            synchronized (Peer.EXIT_LOCK) {
                Peer.EXIT = true;
                Peer.EXIT_LOCK.notify();
            }

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
            int id, long timeStamp, int[] position, int meters, int battery, List<Measurement> pm10_list ) {

        DeliveryService.DeliveryResponse.Builder builder =
                DeliveryService.DeliveryResponse.newBuilder()
                        .setId(id)
                        .setDeliveryTime(timeStamp)
                        .setXPosition(position[0])
                        .setYPosition(position[1])
                        .setMetersDone(meters)
                        .setBatteryLevel(battery);

        for(Measurement m : pm10_list){
            builder.addPm10(
                    DeliveryService.Pm10.newBuilder()
                    .setValue(m.getValue())
                    .setTime(m.getTimestamp())
                    .build()
            );
        }

        return builder.build();
    }
}
