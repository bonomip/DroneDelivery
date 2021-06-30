package GRPC.drone.client;

import GRPC.drone.Peer;
import GRPC.drone.data.FirendList;
import GRPC.drone.server.GreeterImpl;
import REST.beans.drone.Drone;
import drone.grpc.greeterservice.GreetService;
import drone.grpc.greeterservice.GreeterGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Greeter {

    private static void onNext(GreetService.HelloResponse value){
        System.out.println("[ RESPONSE ] drone id "
                +value.getId()+" says master is id "
                +value.getMasterId());

        Peer.setMasterDrone(GreeterImpl.getMasterDroneFromHelloResponse(value));
    }

    private static void onError(ManagedChannel channel, FirendList friends, int drone_id){
        System.out.println("[ ERROR ] Problem with drone id "+drone_id);
        friends.removeWithId(drone_id);
        channel.shutdown();
    }

    private static void onCompleted(ManagedChannel channel, int drone_id){
        System.out.println("[ CONNECTION ] end connection with drone id "+drone_id);
        channel.shutdownNow();
    }

    private static void sendGreetingsToAll(FirendList friends, Drone me, int[] myPosition) throws InterruptedException {

        //the same message is sent to all
        GreetService.HelloRequest req = GreeterImpl.createHelloRequest(me, myPosition);

        ArrayList<ManagedChannel> channels = new ArrayList<>();

        for(Drone friend : friends)
        {
            System.out.println("[ CONNECTION ] creating channel with drone id "+friend.getId());

            final ManagedChannel channel = ManagedChannelBuilder.forTarget(friend.getIp() + ":" + friend.getPort()).usePlaintext().build();
            channels.add(channel);

            GreeterGrpc.GreeterStub stub = GreeterGrpc.newStub(channel);

            stub.greeting(req, new StreamObserver<GreetService.HelloResponse>() {
                @Override
                public void onNext(GreetService.HelloResponse value) {
                    Greeter.onNext(value);
                }

                @Override
                public void onError(Throwable t) {
                    Greeter.onError(channel, friends, friend.getId());
                }

                @Override
                public void onCompleted() {
                    Greeter.onCompleted(channel, req.getId());
                }
            });
        }

        for(ManagedChannel channel : channels){
            channel.awaitTermination(2, TimeUnit.SECONDS);
        }
    }

    public static void joinOverlayNetwork(FirendList friends, Drone me, int[] myPosition) throws InterruptedException, IOException {
        if (friends.size() == 0) // if i'm the first drone in the network
        {
            Peer.setMasterDrone(me);
        }
        else
        {
            sendGreetingsToAll(friends, me, myPosition);// greet all my friends
                                                        // the ones that cant be reached
                                                        // are going to be removed from
                                                        // friend list
            if(friends.size() == 0) //if all my friend were unreachable
                Peer.setMasterDrone(me); //im the only drone in the network.
        }
    }

}
