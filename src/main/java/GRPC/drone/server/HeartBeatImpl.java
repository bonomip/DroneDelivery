package GRPC.drone.server;

import GRPC.drone.Peer;
import com.google.protobuf.Empty;
import drone.grpc.heartbeatservice.HeartBeatGrpc;
import drone.grpc.heartbeatservice.HeartBeatService;
import io.grpc.stub.StreamObserver;

public class HeartBeatImpl extends HeartBeatGrpc.HeartBeatImplBase {

    //MASTER DRONE

    @Override
    public void pulse(Empty request, StreamObserver<Empty> responseObserver) {
        responseObserver.onNext(null);
        responseObserver.onCompleted();
    }
}
