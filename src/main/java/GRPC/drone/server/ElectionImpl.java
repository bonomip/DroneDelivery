package GRPC.drone.server;

import GRPC.drone.Peer;
import GRPC.drone.client.Election;
import GRPC.drone.threads.TBSlave;
import com.google.protobuf.Empty;
import drone.grpc.electionservice.ElectionGrpc;
import drone.grpc.electionservice.ElectionService;
import io.grpc.stub.StreamObserver;

public class ElectionImpl extends ElectionGrpc.ElectionImplBase {

    public static final Object LOCK = new Object();
    public static boolean PARTICIPANT = false;
    public static boolean ELECTION = false;

    @Override
    public void election(ElectionService.ElectionRequest request, StreamObserver<Empty> responseObserver) {
        int my_id = Peer.DATA.getMe().getId();
        int my_battery = Peer.DATA.getRelativeBattery();
        int other_id = request.getId();
        int other_battery = request.getBattery();

        System.out.println("[ELECTION] Me b: " + my_battery + " id: " + my_id +
                " | Other b: " + other_battery + " id: " + other_id);

        if(other_battery > my_battery || (other_battery == my_battery && other_id > my_id))
            try{ Election.forwardElection(request); }
            catch (InterruptedException e){ e.printStackTrace(); }

        if(other_battery < my_battery || ( other_battery == my_battery && other_id < my_id ) && !PARTICIPANT )
            try { Election.forwardElection(getElectionRequest(my_id, my_battery)); }
            catch (InterruptedException e) { e.printStackTrace(); }

        if(other_id == my_id)
            try{ Election.forwardShout(getShoutRequest(my_id)); }
            catch (InterruptedException e ) { e.printStackTrace(); }

        responseObserver.onNext(null);
        responseObserver.onCompleted();
    }

    @Override
    public void shout(ElectionService.ShoutRequest request, StreamObserver<Empty> responseObserver) {
        int master_id = request.getId();
        int my_id = Peer.DATA.getMe().getId();

        System.out.println("[ELECTION] [SHOUT] master id: "+ master_id+" | my id: " + my_id);

        if(master_id == my_id)
            Peer.transitionToMasterDrone();
        else {
            Peer.DATA.setMasterDrone(Peer.MY_FRIENDS.getFromId(master_id));
            synchronized (LOCK){
                ELECTION = false;
                LOCK.notify();
            }
            try {
                Election.forwardShout(request);
            }
            catch (InterruptedException e ) {
                e.printStackTrace();
            }
        }

        responseObserver.onNext(null);
        responseObserver.onCompleted();
    }

    public static ElectionService.ElectionRequest getElectionRequest(int id, int battery){
        return ElectionService.ElectionRequest.newBuilder()
                .setBattery(battery)
                .setId(id)
                .build();
    }

    public static ElectionService.ShoutRequest getShoutRequest(int id){
        return ElectionService.ShoutRequest.newBuilder().setId(id).build();
    }
}
