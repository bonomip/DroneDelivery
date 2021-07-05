package GRPC.drone.client;

import GRPC.drone.Peer;
import GRPC.drone.data.Slave;
import GRPC.drone.server.ElectionImpl;
import REST.beans.drone.Drone;
import com.google.protobuf.Empty;
import drone.grpc.electionservice.ElectionGrpc;
import drone.grpc.electionservice.ElectionService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class Election {

    public static final Object FINISH = new Object();

    public static void onElectionFinished(){
        synchronized (FINISH){
            PARTICIPANT = false;
            FINISH.notify();
        }
    }

    private static boolean CONTINUE = false;
    public static boolean PARTICIPANT = false;

    private static void masterDroneWithNoFriends(){
        System.out.println("MASTER DRONE WITH NO FRIEND");
        Peer.DATA.setMasterDrone(Peer.DATA.getMe());
        Peer.transitionToMasterDrone(Collections.singletonList(new Slave(Peer.DATA)));
        onElectionFinished();
    }

    public static synchronized void startElection() {
        if(PARTICIPANT) {
            System.out.println("[ ELECTION ] [ ALREADY STARTED ]");
            return;
        }

        Peer.DATA.setMasterDrone(null);

        System.out.println("[ ELECTION ] [ START ]");

        if(Peer.MY_FRIENDS.size() == 0)
            masterDroneWithNoFriends();
        else {
            try {
                forwardElection(ElectionImpl.getElectionRequest(
                        Peer.DATA.getMe().getId(), Peer.DATA.getRelativeBattery()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
}

    public synchronized static void forwardElection(ElectionService.ElectionRequest request) throws InterruptedException {

        PARTICIPANT = !request.getShout();

        if(request.getShout()) {
            if(request.getId() == Peer.DATA.getMe().getId())
                Peer.DATA.setMasterDrone(Peer.DATA.getMe());
            else
                Peer.DATA.setMasterDrone(Peer.MY_FRIENDS.getFromId(request.getId()));
        }

        CONTINUE = false;

        while(!CONTINUE) {

            if (Peer.MY_FRIENDS.size() == 0 && !Peer.DATA.isMasterDrone()) {
                masterDroneWithNoFriends();
                return;
            }

            Drone friend = Peer.MY_FRIENDS.getSuccessor(Peer.DATA.getMe().getId());

            String debug = "";
            if(request.getShout())
                debug = " [SHOUT] ";
            System.out.println("[ELECTION]"+debug+"[SEND] " + request.getId() + " to " + friend.getId());


            final ManagedChannel channel = ManagedChannelBuilder
                    .forTarget(friend.getIp() + ":" + friend.getPort()).usePlaintext().build();

            ElectionGrpc.ElectionStub stub = ElectionGrpc.newStub(channel);

            stub.election(request, new StreamObserver<Empty>() {
                @Override
                public void onNext(Empty value) {
                }

                @Override
                public void onError(Throwable t) {
                    System.out.println("[ELECTION] successor is offline " + friend.getId());
                    Peer.MY_FRIENDS.removeWithId(friend.getId());
                    channel.shutdown();
                }

                @Override
                public void onCompleted() {
                    System.out.println("[ELECTION] send correctly to " + friend.getId());
                    CONTINUE = true;
                    if(request.getShout())
                        onElectionFinished();

                    channel.shutdown();
                }
            });

            channel.awaitTermination(20, TimeUnit.SECONDS);
        }
    }
}
