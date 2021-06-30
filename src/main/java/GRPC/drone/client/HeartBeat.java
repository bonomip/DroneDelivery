package GRPC.drone.client;

import GRPC.drone.Peer;
import GRPC.drone.server.HeartBeatImpl;
import drone.grpc.heartbeatservice.HeartBeatGrpc;
import drone.grpc.heartbeatservice.HeartBeatService;
import io.grpc.*;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class HeartBeat {

    private static void onError(Throwable t, int master_id){
        System.out.println("[ ELECTION ] [ START ]");
    }

    public static void beat(String master_ip, int master_port) throws InterruptedException {

        HeartBeatService.HeartRequest request = HeartBeatImpl.createRequest(Peer.ME.getId());

        final ManagedChannel channel = ManagedChannelBuilder.forTarget(master_ip + ":" + master_port).usePlaintext().build();

        HeartBeatGrpc.HeartBeatStub stub = HeartBeatGrpc.newStub(channel);

        stub.pulse(request, new StreamObserver<HeartBeatService.HeartResponse>() {
            @Override
            public void onNext(HeartBeatService.HeartResponse value) {
                //do nothing
            }

            @Override
            public void onError(Throwable t) {
                HeartBeat.onError(t, Peer.MASTER.getId());
            }

            @Override
            public void onCompleted() {
                //do nothing
            }
        });

        channel.awaitTermination(2, TimeUnit.SECONDS);

    }

}
