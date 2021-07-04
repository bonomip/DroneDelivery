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

    private static void onError(Throwable t){
        if(Election.PARTICIPANT) {
            System.out.println("[ ELECTION ] [ ALREADY STARTED ]");
            return;
        }

        System.out.println("MASTER IS DOWN");
        System.out.println("[ ELECTION ] [ START ]");

        Peer.DATA.setMasterDrone(null);

        try {
            Election.startElection(Peer.DATA.getRelativeBattery(), Peer.DATA.getMe().getId());
        }catch (InterruptedException e) { e.printStackTrace();}
    }

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
                HeartBeat.onError(t);
                channel.shutdown();
            }

            @Override
            public void onCompleted() {
                channel.shutdown();
            }
        });

        channel.awaitTermination(2, TimeUnit.SECONDS);
    }

}
