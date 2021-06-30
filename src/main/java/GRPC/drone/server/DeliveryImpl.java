package GRPC.drone.server;
import GRPC.drone.Peer;
import GRPC.drone.client.Deliver;
import SENSOR.Measurement;
import SENSOR.PM10Buffer;
import drone.grpc.deliveryservice.DeliverGrpc;
import drone.grpc.deliveryservice.DeliveryService;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;


public class DeliveryImpl extends DeliverGrpc.DeliverImplBase {

    public static boolean DELIVERING = false;
    public static final Object DELIVERY_LOCK = new Object();

    @Override
    public void assign(
            DeliveryService.DeliveryRequest request,
            StreamObserver<DeliveryService.DeliveryResponse> responseObserver) {

        synchronized (DELIVERY_LOCK) {
            DELIVERING = true;
        }

        int[] origin = new int[] {request.getXOrigin(), request.getYOrigin()};
        int[] destination = new int[] {request.getXDestination(), request.getYDestination()};

        responseObserver.onNext(Peer.executeDelivery(request.getId(), origin, destination));
        responseObserver.onCompleted();

        synchronized (DELIVERY_LOCK) {
            DELIVERING = false;
            DELIVERY_LOCK.notify();
        }

    }

    public static List<Measurement> unpackPm10(DeliveryService.DeliveryResponse value){
        List<Measurement> list = new ArrayList<>();

        for(DeliveryService.Pm10 pm : value.getPm10List())
            list.add(new Measurement(pm.getId(), pm.getType(), pm.getValue(), pm.getTime()));

        return list;
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
            int id, long timeStamp, int[] position, long meters, int battery, List<Measurement> pm10_list ) {

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
                        .setValue((float) m.getValue())
                        .setId(m.getId())
                        .setType(m.getType())
                        .setTime(m.getTimestamp())
                        .build()
            );
        }

        return builder.build();
    }
}
