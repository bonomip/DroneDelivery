package GRPC.drone.data;

import GRPC.drone.Peer;
import REST.beans.drone.Drone;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/*
    is implemented as a Ring List to create the overlay network based on the Ring message protocol
 */

public class FirendList implements  Iterable<Drone> {

    @Override
    public Iterator<Drone> iterator() {
        return new Iterator<Drone>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < elements.size();
            }

            @Override
            public Drone next() {
                return elements.get(currentIndex++);
            }
        };
    }

    //This array will store all elements added to list
    private ArrayList<Drone> elements;

    public FirendList(){
        elements = new ArrayList<>();
    }

    public FirendList(ArrayList<Drone> list){
        this.elements = new ArrayList<>(list);
        this.elements.sort(Comparator.comparingInt(Drone::getId));
    }

    //Add method
    public synchronized boolean add(Drone e) {
        if( containsId(e.getId()) )
            throw new IllegalAccessError("Trying to add drone id "+e.getId()+"," +
                    " \n\t but id is already present");

        for(Drone d : this.elements)
        {
            if (d.getId() > e.getId()) {
                this.elements.add(this.elements.indexOf(d), e);
                return true;
            }
        }

        return this.elements.add(e);

    }

    public synchronized int getIndexFromId(int id){
        for(Drone d : this.elements)
            if(d.getId() == id) return this.elements.indexOf(d);

        throw new IllegalAccessError("Trying to get index for id "+id+"," +
                " \n\t but id is not present");
    }

    private Drone getMod(int index){
        return this.elements.get(index%this.elements.size());
    }

    public synchronized Drone getSuccessor(int id){
        return getMod(getIndexFromId(id)+1);
    }

    public synchronized Drone getPredecessor(int id){
        return getMod(getIndexFromId(id)-1);
    }

    public synchronized void removeWithId(int id){
        if( !containsId(id))
            throw new IllegalAccessError("Trying to remove drone id "+id+"," +
                    " \n\t but id is not present");

        this.elements.removeIf(d -> d.getId() == id);
    }

    //Get method
    public synchronized Drone get(int i) {
        return this.elements.get(i);
    }

    //Remove method
    public synchronized Drone remove(int i) {
        return this.elements.remove(i);
    }

    //Get Size of list
    public synchronized int size() {
        return this.elements.size();
    }

    //Print method
    @Override
    public synchronized String toString()
    {
        return this.elements.toString();
    }

    public synchronized boolean containsId(int id) {//todo can be optimized with divide et impera
        for(Drone d : this.elements)
            if(d.getId() == id)
                return true;

        return false;
    }

    public synchronized void print() {
        for (Drone d : Peer.MY_FRIENDS)
            System.out.println(d.toString());
    }
}
