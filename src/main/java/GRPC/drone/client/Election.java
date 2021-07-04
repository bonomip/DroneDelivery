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

    private static final Object LOCK = new Object(); //lock used to wait for election response

    private static final Object SUCCESSOR_LOCK = new Object();
    private static boolean SUCCESSOR_ON = false;

    public static boolean PARTICIPANT = false;

    public static void startElection(int battery, int id) throws InterruptedException {
        synchronized (ElectionImpl.FINISH){
                if(PARTICIPANT)
                    return;
        }

        if(Peer.MY_FRIENDS.size() == 0)
            Peer.transitionToMasterDrone(null);
        else
            forwardElection(ElectionImpl.getElectionRequest(id, battery, false));
}

    public synchronized static void forwardElection(ElectionService.ElectionRequest request) throws InterruptedException {
        //todo if im going to forward to id 4 a election message with id 4 and the node 4 is offline
        //i restart the election

        if(!request.getShout())
            synchronized (ElectionImpl.FINISH) {
                PARTICIPANT = true;
            }

        do {

            if (Peer.MY_FRIENDS.size() == 0 && !Peer.DATA.isMasterDrone()) {
                Peer.transitionToMasterDrone(null);
                SUCCESSOR_ON = false;
                synchronized (ElectionImpl.FINISH){
                    PARTICIPANT = false;
                    ElectionImpl.FINISH.notify();
                }
                return;
            }

            Drone friend = Peer.MY_FRIENDS.getSuccessor(Peer.DATA.getMe().getId());

            final ManagedChannel channel = ManagedChannelBuilder
                    .forTarget(friend.getIp() + ":" + friend.getPort()).usePlaintext().build();

            ElectionGrpc.ElectionStub stub = ElectionGrpc.newStub(channel);

            if(request.getShout())
                System.out.println("IS A SHOUt");
            System.out.println("[ELECTION] [SEND] " + request.getId() + " to " + friend.getId());

            stub.election(request, new StreamObserver<Empty>() {
                @Override
                public void onNext(Empty value) {
                }

                @Override
                public void onError(Throwable t) {
                    System.out.println("[ELECTION] successor is offline " + friend.getId());

                    Peer.MY_FRIENDS.removeWithId(friend.getId());

                    SUCCESSOR_ON = false;

                    channel.shutdown();
                }

                @Override
                public void onCompleted() {
                    System.out.println("[ELECTION] send correctly to " + friend.getId());
                    SUCCESSOR_ON = true;

                    channel.shutdown();
                }
            });

            channel.awaitTermination(10, TimeUnit.SECONDS);

        } while(!SUCCESSOR_ON);

        SUCCESSOR_ON = false;
    }
}
