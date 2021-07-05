package GRPC.drone.server;

import GRPC.drone.Peer;
import GRPC.drone.data.Slave;
import REST.beans.drone.Drone;
import drone.grpc.greeterservice.GreetService;
import drone.grpc.greeterservice.GreeterGrpc;
import io.grpc.stub.StreamObserver;

public class GreeterImpl extends GreeterGrpc.GreeterImplBase {

    public static final Object LOCK = new Object();

    @Override
    public void greeting(GreetService.HelloRequest request, StreamObserver<GreetService.HelloResponse> responseObserver) {

        GreetService.HelloResponse response;

        if(Peer.DATA.getMaster() == null){
            response = createHelloResponse(Peer.DATA.getMe());
        } else {
            response = createHelloResponse(Peer.DATA.getMe(), Peer.DATA.getMaster());
        }

        Drone drone = getDroneFromHelloRequest(request);

        Peer.MY_FRIENDS.add(drone);

            if (Peer.DATA.isMasterDrone()) {
                if (Peer.MY_SLAVES.isIdInList(drone.getId()))
                    System.out.println("Trying to add drone id " + drone.getId() + " \n\t" +
                            "but drone is already listed as slave");
                else
                    Peer.MY_SLAVES.add(new Slave(drone, getPostionFromHelloRequest(request), 100));
            }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public static Drone getMasterDroneFromHelloResponse(GreetService.HelloResponse response){
        return new Drone(response.getMasterId(), response.getMasterIp(), response.getMasterPort());
    }

    public static Drone getDroneFromHelloRequest(GreetService.HelloRequest request){
        return new Drone(request.getId(), request.getIp(), request.getPort());
    }

    public static int[] getPostionFromHelloRequest(GreetService.HelloRequest request){
        return new int[] {request.getXPosition(), request.getYPosition()};
    }

    public static GreetService.HelloRequest createHelloRequest(Drone me, int[] myPosition){
        return GreetService.HelloRequest.newBuilder()
                .setId(me.getId())
                .setIp(me.getIp())
                .setPort(me.getPort())
                .setXPosition(myPosition[0])
                .setYPosition(myPosition[1])
                .build();
    }

    private static GreetService.HelloResponse createHelloResponse(Drone me, Drone master){
        return GreetService.HelloResponse.newBuilder()
                .setId(me.getId())
                .setMasterId(master.getId())
                .setMasterIp(master.getIp())
                .setMasterPort(master.getPort())
                .build();
    }

    private static GreetService.HelloResponse createHelloResponse(Drone me){
        return GreetService.HelloResponse.newBuilder()
                .setId(me.getId())
                .build();
    }
}
