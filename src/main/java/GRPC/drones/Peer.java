package GRPC.drones;

import GRPC.drones.imp.DeliveryImpl;
import GRPC.drones.imp.GreeterImpl;
import GRPC.drones.service.Greeter;
import GRPC.drones.threads.Behaviour;
import GRPC.drones.threads.TMaster;
import GRPC.drones.data.Slave;
import GRPC.drones.threads.TSlave;
import REST.DroneClient;
import REST.beans.drone.Drone;
import REST.beans.drone.Drones;

import io.grpc.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Peer {

    ///--------------------- actual fields

    public static void setMasterDrone(Drone master){
        MASTER = master;
    }

    public static Drone getMasterDrone(){
        return MASTER;
    }

    public static boolean isMaster(){
        return MASTER.getId() == ME.getId();
    }

    public static Drone MASTER;
    public static Drone ME;
    public static RingList MY_FRIENDS;
    public static ArrayList<Slave> MY_SLAVES = new ArrayList<>();
    public static int[] MY_POSITION;
    public static int BATTERY = 100;
    public static Behaviour THREAD;

    private static Server startGrpcServer() throws IOException {
        Server server = ServerBuilder.forPort(ME.getPort())
                .addService(new GreeterImpl())
                .addService(new DeliveryImpl())
                .build();

        server.start();

        return server;
    }

    private static DroneClient serverRestRegistrationWithDataUnmarshalling(int id, String ip, int port){
        DroneClient client = new DroneClient();

        HashMap<String, Object> data = client.addDroneToNetwork(id, ip, port);

        ME = (Drone) data.get("drone");
        MY_POSITION = (int[]) data.get("position");
        MY_FRIENDS = new RingList(((Drones) data.get("drones")).getList());

        MY_FRIENDS.removeWithId(ME.getId()); //remove myself from the list of the drone in the network

        return client;
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        int id = (int)(Math.random()*100);
        String ip = "localhost";
        int port = (int)(Math.random()*2000);   //in this case, we are using the same ip for all the drones,
                                                //so all the ports trought all the application need to be different

        DroneClient restClient = serverRestRegistrationWithDataUnmarshalling(id, ip, port);

        Server grpcServer = startGrpcServer();

        Greeter.joinOverlayNetwork(MY_FRIENDS, ME, MY_POSITION);

        MY_SLAVES.add(new Slave(ME, MY_POSITION));

        if(isMaster())
            THREAD = new TMaster();
        else
            THREAD = new TSlave();

        THREAD.start();

        //LIFE CYCLE
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true){

            THREAD.printStatus();

            String in = br.readLine();
            if(in.equals("quit")){
                System.out.println("[ INFO ] quitting drone id "+ ME.getId());
                restClient.removeDroneFromNetwork(ME);
                grpcServer.shutdown();
                THREAD.quit();
                break;
            }
        }
    }


}
