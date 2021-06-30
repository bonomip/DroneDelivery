package GRPC.drone.server;

import GRPC.drone.Peer;
import drone.grpc.heartbeatservice.HeartBeatGrpc;
import drone.grpc.heartbeatservice.HeartBeatService;
import io.grpc.stub.StreamObserver;

public class HeartBeatImpl extends HeartBeatGrpc.HeartBeatImplBase {

    //MASTER DRONE

    @Override
    public void pulse(HeartBeatService.HeartRequest request, StreamObserver<HeartBeatService.HeartResponse> responseObserver) {
        HeartBeatService.HeartResponse response = createResponse(Peer.ME.getId());

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }


    public static HeartBeatService.HeartRequest createRequest(int id){
        return HeartBeatService.HeartRequest.newBuilder()
                .setId(id)
                .build();
    }


    public static HeartBeatService.HeartResponse createResponse(int id){
        return HeartBeatService.HeartResponse.newBuilder()
                .setId(id)
                .build();
    }
}
