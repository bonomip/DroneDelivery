package GRPC.drone.data;

import GRPC.drone.Peer;
import GRPC.drone.client.Deliver;
import GRPC.drone.data.stat.DeliveryStat;
import REST.beans.statistic.Statistic;
import SENSOR.Measurement;
import drone.grpc.electionservice.ElectionService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SlaveList {


    private final ArrayList<Slave> list;

    public SlaveList(){
        list = new ArrayList<>();
    }

    public SlaveList(List<Slave> list){
        this.list = new ArrayList<>(list);
    }

    public synchronized void add(Slave s){
        this.list.add(s);
    }

    public synchronized Slave getDroneWithId(int id) {
        for(Slave s : this.list){
            if(s.getId() == id)
                return s;
        }
        return null;
    }

    public synchronized int size(){
        return this.list.size();
    }

    public synchronized void removeIdFromList(int slave_id){
        this.list.removeIf(s -> s.getId() == slave_id);
    }

    public synchronized boolean isIdInList(int slave_id){
        return this.list.stream().anyMatch(s -> s.getId() == slave_id);
    }

    public synchronized List<Integer> getSlaveNotInDelivery(){
        List<Integer> result = new LinkedList<>();
        for(Slave s : this.list)
            if(!s.isDelivering())
                result.add(s.getId());
        return result;
    }

    public synchronized List<Integer> getNearestSlave(List<Integer> sub_list, int[] position){
        List<Integer> result = new LinkedList<>();

        double min = 1000;

        for(int i : sub_list){
            Slave s = this.getDroneWithId(i);

            if(s == null)
                continue;

            double curr = Deliver.distance(s.getPosition(), position);

            if (curr < min){
                min = curr;
                result.clear();
                result.add(i);
            } else if (curr == min)
                result.add(i);
        }

        return result;
    }

    public synchronized List<Integer> getMostCharged(List<Integer> sub_list){

        int max = 0;
        List<Integer> result = new LinkedList<>();

        for(int i : sub_list){
            Slave s = this.getDroneWithId(i);
            if(s == null)
                continue;

            int curr = s.getBattery();

            if (curr > max){
                max = curr;
                result.clear();
                result.add(i);
            } else if (curr == max) {
                result.add(i);
            }
        }
        return result;
    }

    public synchronized Statistic getGlobalStatistic(){

        ArrayList<DeliveryStat> tmp = new ArrayList<>();

        float d = 0f;
        int p_c = 0;
        float p = 0f;
        int m = 0;
        float b = 0f;

        for(Slave s : list) {
            tmp = s.getSlaveDeliveryStats();

            d += tmp.size();

            for(DeliveryStat ds : tmp){
                p_c += ds.getPm10().size();
                p += ds.getPm10().stream().mapToDouble(Measurement::getValue).sum();
            }

            m += tmp.stream().mapToLong(DeliveryStat::getMetres).sum();

            b += s.getBattery();
        }

        if(d == 0f) return null;

        return new Statistic(d/list.size(), m/list.size(), p/p_c, b/list.size());
    }

    public synchronized void print() {
        for(Slave s : Peer.MY_SLAVES.list)
            System.out.println(s.toString());
    }
}
