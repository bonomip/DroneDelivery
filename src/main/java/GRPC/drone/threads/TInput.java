package GRPC.drone.threads;

import GRPC.drone.Peer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TInput extends Thread {
    @Override
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Peer.BTHREAD.printStatus();

        //noinspection InfiniteLoopStatement
        while (true) {
            String in = "quit";
            try {
                in = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (in.equals("quit"))
                synchronized (Peer.EXIT_LOCK) {
                    if(Peer.isMaster())
                        Peer.MY_SLAVES.removeIdFromList(Peer.ME.getId());
                    Peer.EXIT = true;
                    Peer.EXIT_LOCK.notify();
                }
            else{
                Peer.BTHREAD.printStatus();
            }
        }
    }
}
