package GRPC.drone.client;

import GRPC.drone.Peer;
import GRPC.drone.server.ElectionImpl;
import REST.beans.drone.Drone;
import com.google.protobuf.Empty;
import drone.grpc.electionservice.ElectionGrpc;
import drone.grpc.electionservice.ElectionService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class Election {

    static boolean FOUND_SUCCESSOR = false;

    public static void startElection(int battery, int id) throws InterruptedException {

        synchronized (ElectionImpl.LOCK){
            if(ElectionImpl.PARTICIPANT) return;
        }

        ElectionService.ElectionRequest request = ElectionImpl.getElectionRequest(id, battery);

        forwardElection(request);
}

public static void forwardElection(ElectionService.ElectionRequest request) throws InterruptedException {
    synchronized (ElectionImpl.LOCK){
        ElectionImpl.PARTICIPANT = true;
    }

    do {

        if(Peer.MY_FRIENDS.size() == 0) {
            Peer.transitionToMasterDrone();
            synchronized (ElectionImpl.LOCK){
                ElectionImpl.PARTICIPANT = false;
            }
            return;
        }

        Drone friend = Peer.MY_FRIENDS.getSuccessor(Peer.DATA.getMe().getId());

        final ManagedChannel channel = ManagedChannelBuilder
                .forTarget(friend.getIp() + ":" + friend.getPort()).usePlaintext().build();

        ElectionGrpc.ElectionStub stub = ElectionGrpc.newStub(channel);

        stub.election(request, new StreamObserver<Empty>() {
            @Override
            public void onNext(Empty value) {
                FOUND_SUCCESSOR = true;
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("[ELECTION] successor is offline " + friend.getId());
                Peer.MY_FRIENDS.removeWithId(friend.getId());
            }

            @Override
            public void onCompleted() {
                channel.shutdown();
            }
        });

        channel.awaitTermination(2, TimeUnit.SECONDS);

    } while(!FOUND_SUCCESSOR);
}

public static void forwardShout(ElectionService.ShoutRequest request) throws InterruptedException {
    synchronized (ElectionImpl.LOCK){
        ElectionImpl.PARTICIPANT = true;
    }

    do {
        Drone friend = Peer.MY_FRIENDS.getSuccessor(Peer.DATA.getMe().getId());

        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(friend.getIp() + ":" + friend.getPort()).usePlaintext().build();

        ElectionGrpc.ElectionStub stub = ElectionGrpc.newStub(channel);

        stub.shout(request, new StreamObserver<Empty>() {
            @Override
            public void onNext(Empty value) {

            }

            @Override
            public void onError(Throwable t) {
                //if friend is offline
                //remove it from friend list
                Peer.MY_FRIENDS.removeWithId(friend.getId());
            }

            @Override
            public void onCompleted() {
                FOUND_SUCCESSOR = true;
            }
        });

        channel.awaitTermination(1, TimeUnit.SECONDS);

    } while(!FOUND_SUCCESSOR);
}

}
