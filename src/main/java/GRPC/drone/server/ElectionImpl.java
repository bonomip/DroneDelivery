package GRPC.drone.server;

import GRPC.drone.Peer;
import GRPC.drone.client.Election;
import com.google.protobuf.Empty;
import drone.grpc.electionservice.ElectionGrpc;
import drone.grpc.electionservice.ElectionService;
import io.grpc.stub.StreamObserver;

public class ElectionImpl extends ElectionGrpc.ElectionImplBase {

    public static final Object FINISH = new Object();

    public static final Object LOCK = new Object();
    public static boolean STOP = false;

    @Override
    public void election(ElectionService.ElectionRequest request, StreamObserver<Empty> responseObserver) {
        int my_id = Peer.DATA.getMe().getId();
        int my_battery = Peer.DATA.getRelativeBattery();
        int other_id = request.getId();
        int other_battery = request.getBattery();

        if(!request.getShout()) {
            System.out.println("[ELECTION] [RECEIVED]Me id: " + my_id + " battery: " + my_battery +
                    " | Other id: " + other_id + " id: " + other_battery);

            //todo move all login into Election class

            if (other_battery > my_battery || (other_battery == my_battery && other_id > my_id))
                new Thread(() -> {
                    try {
                        Election.forwardElection(request);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();

            if ((other_battery < my_battery || (other_battery == my_battery && other_id < my_id)) && !Election.PARTICIPANT)
                new Thread(() -> {
                    try {
                        Election.forwardElection(getElectionRequest(my_id, my_battery, false));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();

            if (other_id == my_id)
                new Thread(() -> {
                    try {
                        Election.forwardElection(getElectionRequest(my_id, my_battery, true));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();

        } else { //election is over

            if(other_id == my_id) {
                Peer.transitionToMasterDrone(request);
            }
            else {
                System.out.println("[ELECTION] [SHOUT] MASTER IS "+ other_id);
                Peer.DATA.setMasterDrone(Peer.MY_FRIENDS.getFromId(other_id));
                new Thread(() -> {
                    try {
                        Election.forwardElection(request);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }

            synchronized (FINISH){
                Election.PARTICIPANT = false;
                FINISH.notify();
            }

        }

        responseObserver.onNext(null);
        responseObserver.onCompleted();
    }

    public static ElectionService.ElectionRequest getElectionRequest(int id, int battery, boolean isShout){
        return ElectionService.ElectionRequest.newBuilder()
                .setBattery(battery)
                .setId(id)
                .setShout(isShout)
                .build();
    }
}
