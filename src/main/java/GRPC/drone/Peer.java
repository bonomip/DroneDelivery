package GRPC.drone;

import GRPC.drone.client.Deliver;
import GRPC.drone.data.*;
import GRPC.drone.server.DeliveryImpl;
import GRPC.drone.server.GreeterImpl;
import GRPC.drone.client.Greeter;
import GRPC.drone.server.HeartBeatImpl;
import GRPC.drone.threads.Behaviour;
import GRPC.drone.threads.TBMaster;
import GRPC.drone.threads.TInput;
import GRPC.drone.threads.TBSlave;
import REST.DroneClient;
import REST.beans.drone.Drone;
import REST.beans.drone.Drones;

import SENSOR.Measurement;
import SENSOR.PM10Buffer;
import SENSOR.PM10Simulator;
import drone.grpc.deliveryservice.DeliveryService;
import io.grpc.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Peer {

    ///--------------------- actual fields

    public static Data DATA = new Data();

    public static FirendList MY_FRIENDS;
    public static SlaveList MY_SLAVES = new SlaveList();

    public static Behaviour BTHREAD;
    public static TInput ITHREAD;

    public static boolean EXIT = false;
    public static final Object EXIT_LOCK = new Object();

    public static DroneClient REST_CLIENT;
    public static Server GRPC_SERVER;

    public static PM10Simulator SENSOR;
    public static PM10Buffer SENSOR_BUFFER;

    public static DeliveryService.DeliveryResponse executeDelivery(int id, int[] origin, int[] destination){

        System.out.println("\t\t\t[ DELIVERY ] "+id+" [ RECEIVED ]");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long meters =
                ( (long) Deliver.distance(origin, destination) ) * 1000L +
                        ( (long) Deliver.distance( DATA.getPosition(), origin ));

        long time = System.currentTimeMillis();

        List<Measurement> pm10 = SENSOR_BUFFER.readAllAndClean();

        DATA.setBattery(DATA.getBattery() - 10);
        DATA.setDeliveries( DATA.getDeliveries() + 1 );
        DATA.setMetres(DATA.getMetres() + meters);
        DATA.setPosition(destination);

        System.out.println("\t\t\t[ DELIVERY ] "+id+" [ DONE ]");

        if(DATA.getBattery() < 15)
            synchronized (EXIT_LOCK) {
                EXIT = true;
                EXIT_LOCK.notify();
            }

        return DeliveryImpl.createDeliveryResponse(id, time, destination, meters, DATA.getBattery(), pm10);
    }

    private static void quitThreads(){
        BTHREAD.quit();
        SENSOR.stopMeGently();
        try {
            BTHREAD.join();
            SENSOR.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void exit(){
        System.out.println("\t\t[ QUIT ] [ START ] id "+ DATA.getMe().getId());

        if(DATA.isMasterDrone()) {
            quitThreads();

            System.out.println("\t\t[ QUIT ] block incoming grpc");
            GRPC_SERVER.shutdown();

            System.out.println("\t\t[ QUIT ] send global stats to server");
            REST_CLIENT.sendInfo(MY_SLAVES.getGlobalStatistic());
        } else {

            System.out.println("\t\t[ QUIT ] block incoming grpc");
            GRPC_SERVER.shutdown();

            quitThreads();
        }

        System.out.println("\t\t[ QUIT ] remove drone from network");
        REST_CLIENT.removeDroneFromNetwork(DATA.getMe());

        System.out.println("\t\t[ QUIT ] [ FINISH ] id " + DATA.getMe().getId());

        System.exit(0);
    }

    private static void setUpSensor(){
        SENSOR_BUFFER = new PM10Buffer();
        SENSOR = new PM10Simulator(SENSOR_BUFFER);
        SENSOR.start();
    }

    private static Server startGrpcServer() throws IOException {
        Server server = ServerBuilder.forPort(DATA.getMe().getPort())
                .addService(new GreeterImpl())
                .addService(new DeliveryImpl())
                .addService(new HeartBeatImpl())
                .build();

        server.start();

        return server;
    }

    private static DroneClient serverRestRegistrationWithDataUnmarshalling(int id, String ip, int port){
        DroneClient client = new DroneClient();

        HashMap<String, Object> data = client.addDroneToNetwork(id, ip, port);

        DATA.setMe((Drone) data.get("drone"));

        DATA.setPosition((int[]) data.get("position"));
        DATA.setBattery(100);

        MY_FRIENDS = new FirendList(((Drones) data.get("drones")).getList());
        MY_FRIENDS.removeWithId(DATA.getMe().getId()); //remove myself from the list of the drone in the network

        return client;
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        int id = (int)(Math.random()*100);
        String ip = "localhost";
        int port = (int)(Math.random()*2000);   //in this case, we are using the same ip for all the drones,
                                                //so all the ports through all the application need to be different

        REST_CLIENT = serverRestRegistrationWithDataUnmarshalling(id, ip, port);

        GRPC_SERVER = startGrpcServer();

        setUpSensor();

        Greeter.joinOverlayNetwork(MY_FRIENDS, DATA.getMe(), DATA.getPosition());

        if(DATA.isMasterDrone()) {
            BTHREAD = new TBMaster();
            MY_SLAVES.add(new Slave(DATA.getMe(), DATA.getPosition(), DATA.getBattery()));
        }
        else
            BTHREAD = new TBSlave();

        BTHREAD.start();

        //thread for handle input from keyboard
        ITHREAD = new TInput();
        ITHREAD.start();

        synchronized (EXIT_LOCK){
            while(!EXIT){
                EXIT_LOCK.wait();
            }
            exit();
        }
    }

    public static void transitionToMasterDrone() {
        System.out.println("[ELECTION] I'M THE NEW MASTER");

        //todo

        /*
        synchronized (ElectionImpl.LOCK){
                   ElectionImpl.ELECTIOn = false;
                        ElectionImpl.LOCK.wait();
                }
         */
    }


}
