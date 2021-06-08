package GRPC.drones;

import REST.beans.drone.Drone;
import drone.grpc.greeterservice.DroneService;
import drone.grpc.greeterservice.GreeterGrpc;
import io.grpc.stub.StreamObserver;

public class GreeterImpl extends GreeterGrpc.GreeterImplBase {

    @Override
    public void greeting(DroneService.HelloRequest request, StreamObserver<DroneService.HelloResponse> responseObserver) {
        DroneService.HelloResponse response = createHelloResponse(Peer.ME, Peer.MASTER);

        Drone drone = getDroneFromHelloRequest(request);

        Peer.MY_FRIENDS.add(drone);

        if(Peer.isMaster())
        {
            if(Peer.MY_SLAVES.containsKey(drone))
                System.out.println("Trying to add drone id "+drone.getId()+" \n\t" +
                        "but drone is already listed as slave");
            else
                Peer.MY_SLAVES.put(drone, getPostionFromHelloRequest(request));
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    static Drone getMasterDroneFromHelloResponse(DroneService.HelloResponse response){
        return new Drone(response.getMasterId(), response.getMasterIp(), response.getMasterPort());
    }

    static Drone getDroneFromHelloRequest(DroneService.HelloRequest request){
        return new Drone(request.getId(), request.getIp(), request.getPort());
    }

    static int[] getPostionFromHelloRequest(DroneService.HelloRequest request){
        return new int[] {request.getXPosition(), request.getYPosition()};
    }

    static DroneService.HelloRequest createHelloRequest(Drone me, int[] myPosition){

        return DroneService.HelloRequest.newBuilder()
                .setId(me.getId())
                .setIp(me.getIp())
                .setPort(me.getPort())
                .setXPosition(myPosition[0])
                .setYPosition(myPosition[1])
                .build();
    }

    private static DroneService.HelloResponse createHelloResponse(Drone me, Drone master){
        return DroneService.HelloResponse.newBuilder()
                .setId(me.getId())
                .setMasterId(master.getId())
                .setMasterIp(master.getIp())
                .setMasterPort(master.getPort())
                .build();
    }

}
