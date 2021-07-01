package GRPC.drone.client;

import GRPC.drone.Peer;
import GRPC.drone.server.HeartBeatImpl;
import com.google.protobuf.Empty;
import drone.grpc.heartbeatservice.HeartBeatGrpc;
import drone.grpc.heartbeatservice.HeartBeatService;
import io.grpc.*;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class HeartBeat {

    private static void onError(Throwable t){
        System.out.println("[ ELECTION ] [ START ]");
    }

    public static void beat(String master_ip, int master_port) throws InterruptedException {

        final ManagedChannel channel = ManagedChannelBuilder.forTarget(master_ip + ":" + master_port).usePlaintext().build();

        HeartBeatGrpc.HeartBeatStub stub = HeartBeatGrpc.newStub(channel);

        stub.pulse(null, new StreamObserver<Empty>() {
            @Override
            public void onNext(Empty value) {

            }

            @Override
            public void onError(Throwable t) {
                onError(t);
            }

            @Override
            public void onCompleted() {

            }
        });

        channel.awaitTermination(2, TimeUnit.SECONDS);

    }

}
