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
        System.out.println("[ ELECTION ] [ START ]");

        Peer.MY_FRIENDS.removeWithId(Peer.DATA.getMaster().getId());
        Peer.DATA.setMasterDrone(null);

        synchronized (ElectionImpl.LOCK){
            ElectionImpl.ELECTION = true;
        }

        try {
            Election.startElection(Peer.DATA.getRelativeBattery(), Peer.DATA.getMe().getId());
        }catch (InterruptedException e) { e.printStackTrace();}
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
                HeartBeat.onError(t);
            }

            @Override
            public void onCompleted() {
                channel.shutdown();
            }
        });

        channel.awaitTermination(2, TimeUnit.SECONDS);
    }

}
