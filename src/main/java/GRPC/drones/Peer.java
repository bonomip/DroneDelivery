package GRPC.drones;

import REST.DroneClient;
import REST.beans.drone.Drone;
import REST.beans.drone.Drones;

import drone.grpc.greeterservice.DroneService;
import drone.grpc.greeterservice.GreeterGrpc;
import io.grpc.*;
import io.grpc.stub.StreamObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Peer {

    ///--------------------- actual fields

    private static void setMasterDrone(Drone master){
        MASTER = master;
    }

    private static Drone getMasterDrone(){
        return MASTER;
    }

    public static boolean isMaster(){
        return MASTER.getId() == ME.getId();
    }

    public static Drone MASTER;

    public static Drone ME;
    public static RingList MY_FRIENDS;
    public static HashMap<Drone, int[]> MY_SLAVES;
    public static int[] MY_POSITION;
    public static int ENERGY = 100;
    static int ID, PORT;
    static String IP;

    public static void printStatus(){
        if(isMaster()) {
            System.out.println("---- I'M THE MASTER "+ME.getId());
            System.out.println("-------- MY SLAVES ARE:");
            for(Map.Entry<Drone, int[]> e : MY_SLAVES.entrySet()){
                System.out.print(e.getKey().toString());
                System.out.println(" @ "+ Arrays.toString(e.getValue()));
            }
            System.out.println("-------- MY FRIENDS ARE:");
            for(Drone d : MY_FRIENDS){
                System.out.println(d.toString());
            }
        } else {
            System.out.println("---- I'M A SLAVE "+ME.getId());
            System.out.println("-------- MY MASTER IS:");
            System.out.println(MASTER.toString());
            System.out.println("-------- MY FRIENDS ARE:");
            for(Drone d : MY_FRIENDS){
                System.out.println(d.toString());
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        //todo id ip port redundant after getting "ME" drone (that contains the same data)
        ID = (int)(Math.random()*100);
        IP = "localhost";
        PORT = (int)(Math.random()*2000); //in this case, we are using the same ip for all the drones,
        //so all the ports trought all the application need to be different

        //used when master
        MY_SLAVES = new HashMap<>();

        //REST CLIENT ADD DRONE
        DroneClient rest = new DroneClient();

        HashMap<String, Object> data = rest.addDroneToNetwork(ID, IP, PORT);
        //get data from  server when adding drone to it
        ME = (Drone) data.get("drone");
        MY_POSITION = (int[]) data.get("position");
        //get list of drones from server
        //remove myself from the list of the drone in the network
        Drones drones = (Drones) data.get("drones");
        MY_FRIENDS = new RingList(drones.getList());
        MY_FRIENDS.removeWithId(ID);

        //SERVER CLIENT GRPC PROCEDURES
        MASTER = joinOverlayNetwork(MY_FRIENDS, PORT, ME, MY_POSITION);

        //todo if im the master im also a potentially slave

        //LIFE CYCLE
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true){

            printStatus();

            String in = br.readLine();
            if(in.equals("quit")){
                System.out.println("[ INFO ] quitting drone id "+ID);
                rest.removeDroneFromNetwork(ME);
                break;
            }
        }
    }

    private static void onGreetResponse(DroneService.HelloResponse value){
        System.out.println("[ RESPONSE ] drone id "
                +value.getId()+" says master is id "
                +value.getMasterId());

        setMasterDrone(GreeterImpl.getMasterDroneFromHelloResponse(value));
    }

    private static void onGreetError(RingList friends, int drone_id){
        System.out.println("[ ERROR ] Problem with drone id "+drone_id);
        friends.removeWithId(drone_id);
    }

    private static void onGreetCompleted(ManagedChannel channel, int drone_id){
        System.out.println("[ CONNECTION ] end connection with drone id "+drone_id);
        channel.shutdownNow();
    }

    public static Drone sendGreetingsToAll(RingList friends, Drone me, int[] myPosition) throws InterruptedException {

        //the same message is sent to all
        DroneService.HelloRequest req = GreeterImpl.createHelloRequest(ME, myPosition);

        for(Drone friend : friends)
        {
            System.out.println("[ CONNECTION ] creating channel with drone id "+friend.getId());

            final ManagedChannel channel = ManagedChannelBuilder.forTarget(friend.getIp() + ":" + friend.getPort()).usePlaintext().build();

            GreeterGrpc.GreeterStub stub = GreeterGrpc.newStub(channel);

            stub.greeting(req, new StreamObserver<DroneService.HelloResponse>() {
                @Override
                public void onNext(DroneService.HelloResponse value) {
                    onGreetResponse(value);
                }

                @Override
                public void onError(Throwable t) {
                    onGreetError(friends, req.getId());
                }

                @Override
                public void onCompleted() {
                    onGreetCompleted(channel, req.getId());
                }
            });

            channel.awaitTermination(1, TimeUnit.SECONDS);
        }

        return getMasterDrone();

    }

    private static Server createAndStartServer(int port) throws IOException {
        Server server = ServerBuilder.forPort(port).addService(new GreeterImpl()).build();
        server.start();
        return server;
    }

    public static Drone joinOverlayNetwork(RingList friends, int port, Drone me, int[] myPosition) throws InterruptedException, IOException {
        Server server = createAndStartServer(port);
        Drone master;

        if(friends.size() == 0)
            master = me;
        else
            master = sendGreetingsToAll(friends, me, myPosition);

        return master;
    }


}
