package GRPC.drone.test;

import GRPC.drone.Peer;
import GRPC.drone.client.Election;
import GRPC.drone.client.HeartBeat;
import GRPC.drone.server.DeliveryImpl;
import GRPC.drone.server.ElectionImpl;
import GRPC.drone.server.GreeterImpl;
import GRPC.drone.server.HeartBeatImpl;
import com.google.protobuf.Empty;
import drone.grpc.heartbeatservice.HeartBeatGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TestSynchAwait {


    public static void main(String[] args) throws IOException {

        String ip = "localhost";
        int port = 1234;
        int port2 = 1235;

        Server server = ServerBuilder.forPort(port)
                .addService(new HeartBeatImpl())
                .build();

        server.start();

        Server server2 = ServerBuilder.forPort(port2)
                .addService(new HeartBeatImpl())
                .build();

        server2.start();
        new Thread(() -> method(ip, port, "uno")).start();

        new Thread(() -> method(ip, port2, "due")).start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("EXIT");


    }


    public static synchronized void method(String ip, int port, String s){

        System.out.println("[INSIDE METHOD]"+s);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final ManagedChannel channel = ManagedChannelBuilder.forTarget(ip + ":" +port).usePlaintext().build();

        HeartBeatGrpc.HeartBeatStub stub = HeartBeatGrpc.newStub(channel);

        stub.pulse(null, new StreamObserver<Empty>() {
            @Override
            public void onNext(Empty value) {
                System.out.println("ONNEXT");
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("ERROR");
                channel.shutdown();
            }

            @Override
            public void onCompleted() {
                System.out.println("COMPLETED");
                channel.shutdown();
            }
        });

        try {
            channel.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("[OUTSIDE METHOD]"+s);
    }
}
