package GRPC.drone.threads;

import GRPC.drone.Peer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TInput extends Thread {
    @Override
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {

            String in = "quit";

            try {
                in = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (in.equals("quit") || in.equals("q"))
                synchronized (Peer.EXIT_LOCK) {
                    if(Peer.DATA.isMasterDrone()) //this is for delivry ecc.
                        Peer.MY_SLAVES.removeIdFromList(Peer.DATA.getMe().getId());
                    Peer.EXIT = true;
                    Peer.EXIT_LOCK.notify();
                    break;
            }
        }
    }
}
