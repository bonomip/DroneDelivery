package GRPC.drone.data;

import GRPC.drone.Peer;

import java.util.ArrayList;

public class SlaveList {


    private final ArrayList<Slave> list;

    public SlaveList(){
        list = new ArrayList<>();
    }

    public synchronized void add(Slave s){
        this.list.add(s);
    }

    public synchronized int size(){
        return this.list.size();
    }

    public synchronized void removeIdFromList(int slave_id){
        this.list.removeIf(s -> s.drone.getId() == slave_id);
    }

    public synchronized boolean isIdInList(int slave_id){
        return this.list.stream().anyMatch(s -> s.drone.getId() == slave_id);
    }

    public void print() {
        for(Slave s : Peer.MY_SLAVES.list)
            System.out.println(s.toString());
    }
}
