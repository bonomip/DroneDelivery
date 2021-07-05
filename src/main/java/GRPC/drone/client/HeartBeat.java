package GRPC.drone.client;

import GRPC.drone.Peer;
import GRPC.drone.server.DeliveryImpl;
import GRPC.drone.server.ElectionImpl;
import GRPC.drone.server.HeartBeatImpl;
import GRPC.drone.threads.TBSlave;
import com.google.protobuf.Empty;
import drone.grpc.heartbeatservice.HeartBeatGrpc;
import drone.grpc.heartbeatservice.HeartBeatService;
import io.grpc.*;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class HeartBeat {

    public static void beat(String master_ip, int master_port) throws InterruptedException {

        System.out.println("[HEARTBEAT] to " + Peer.DATA.getMaster().getId() );

        final ManagedChannel channel = ManagedChannelBuilder.forTarget(master_ip + ":" + master_port).usePlaintext().build();

        HeartBeatGrpc.HeartBeatStub stub = HeartBeatGrpc.newStub(channel);

        stub.pulse(null, new StreamObserver<Empty>() {
            @Override
            public void onNext(Empty value) {
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("MASTER IS DOWN");
                new Thread(Election::startElection).start();
                channel.shutdown();
            }

            @Override
            public void onCompleted() {
                channel.shutdown();
            }
        });

        channel.awaitTermination(10, TimeUnit.SECONDS);
    }

}
