package GRPC.drone.server;

import GRPC.drone.Peer;
import GRPC.drone.client.Election;
import GRPC.drone.data.Slave;
import REST.beans.drone.Drone;
import com.google.protobuf.Empty;
import drone.grpc.electionservice.ElectionGrpc;
import drone.grpc.electionservice.ElectionService;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

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
            Peer.DATA.setMasterDrone(null);

            System.out.println("[ELECTION] [RECEIVED]Me id: " + my_id + " battery: " + my_battery +
                    " | Other id: " + other_id + " id: " + other_battery);

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
                        Election.forwardElection(getElectionRequest(my_id, my_battery));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();

            if (other_id == my_id) {
                Peer.DATA.setMasterDrone(Peer.DATA.getMe());
                new Thread(() -> {
                    try {
                        Election.forwardElection(getShoutRequest(my_id, my_battery));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } else { //election is over

            System.out.println("[ELECTION] [SHOUT] [RECEIVED] Me id: " + my_id + " battery: " + my_battery +
                    " | Other id: " + other_id + " id: " + other_battery);

            if(other_id == my_id) {
                Peer.transitionToMasterDrone(fromShoutToSlaveList(request));
            }
            else {
                System.out.println("[ELECTION] [SHOUT] MASTER IS "+ other_id);
                Peer.DATA.setMasterDrone(Peer.MY_FRIENDS.getFromId(other_id));
                new Thread(() -> {
                    try {
                        Election.forwardElection(addDataToShout(request));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }

            synchronized (FINISH){
                System.out.println("notify heatbeat to start again");
                FINISH.notify();
            }
        }

        responseObserver.onNext(null);
        responseObserver.onCompleted();
    }


    private static List<Slave> fromShoutToSlaveList(ElectionService.ElectionRequest shout){

        ArrayList<Slave> result = new ArrayList<>();

        for(ElectionService.SlaveInfo info : shout.getSlavesList()){
            Drone drone;

            if(info.getId() == Peer.DATA.getMe().getId())
                drone = Peer.DATA.getMe();
            else
                drone = Peer.MY_FRIENDS.getFromId(info.getId());

            int[] pos = new int[]{ info.getPositionX(), info.getPositionY() };
            int battery = info.getBattery();

            result.add(new Slave(drone, pos, battery));
        }

        if(result.size() == 0) {
            try {
                throw new IllegalAccessException("ERROR! NO SLAVE FROM SHOUT");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return  result;
    }

    public static ElectionService.ElectionRequest getElectionRequest(int id, int battery){
        return ElectionService.ElectionRequest.newBuilder()
                .setBattery(battery)
                .setId(id)
                .setShout(false)
                .build();
    }

    public static ElectionService.ElectionRequest getShoutRequest(int id, int battery){

        return ElectionService.ElectionRequest.newBuilder()
                .setBattery(battery)
                .setId(id)
                .setShout(true)
                .addSlaves(
                        ElectionService.SlaveInfo.newBuilder()
                                .setId(Peer.DATA.getMe().getId())
                                .setPositionX(Peer.DATA.getPosition()[0])
                                .setPositionY(Peer.DATA.getPosition()[1])
                                .setBattery(Peer.DATA.getRelativeBattery())
                                .build()
                ).build();
    }

    public static ElectionService.ElectionRequest addDataToShout(ElectionService.ElectionRequest request){

        ElectionService.ElectionRequest.Builder builder = ElectionService.ElectionRequest.newBuilder()
                .setBattery(request.getBattery())
                .setId(request.getId())
                .setShout(true);

        for(ElectionService.SlaveInfo info : request.getSlavesList())
            builder.addSlaves(info);

        return builder.addSlaves( ElectionService.SlaveInfo.newBuilder()
                .setId(Peer.DATA.getMe().getId())
                .setPositionX(Peer.DATA.getPosition()[0])
                .setPositionY(Peer.DATA.getPosition()[1])
                .setBattery(Peer.DATA.getRelativeBattery())
                .build()
        ).build();
    }
}
