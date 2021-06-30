package REST.beans.statistic;

import REST.AdminServer;
import REST.utils.DataFormatter;

import javax.swing.text.DateFormatter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Statistics {
    private ArrayList<Statistic> stats;

    public Statistics(){
        this.stats = new ArrayList<>();
    }

    public Statistics(List<Statistic> list) { this.stats = new ArrayList<>(list); }

    public Statistics getLast(int n) {
        if(n <= 0) return new Statistics();
        if(n >= this.stats.size()) return new Statistics(this.stats);
        return new Statistics(this.stats.subList(this.stats.size()-n, this.stats.size()));
    }

    public float getAvgKm(int t1, int t2) {
        long lt1 = AdminServer.START_TIME + (t1 * 60000L);
        long lt2 = AdminServer.START_TIME + (t2 * 60000L);

        float c = 0f;
        long m = 0;

        for(Statistic s : this.stats){
            if(s.getTime() > lt2) break;
            if(s.getTime() >= lt1) {
                c++;
                m += s.getMetres();
            }
        }

        if(c == 0)
            return 0.0f;

        return (m/c)/1000f;
    }

    public float getAvgDeliveries(int t1, int t2) {
        long lt1 = AdminServer.START_TIME + (t1 * 60000L);
        long lt2 = AdminServer.START_TIME + (t2 * 60000L);

        float c = 0f;
        int d = 0;
        for(Statistic s : this.stats){
            if(s.getTime() > lt2) break;
            if(s.getTime() >= lt1) {
                c++;
                d += s.getDeliveries();
            }
        }

        if(c == 0)
            return 0.0f;

        return d/c;
    }

    public ArrayList<Statistic> getList(){
        return this.stats;
    }

    public boolean add(Statistic stat) {
        return this.stats.add(stat);
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "statList=" + stats +
                '}';
    }
}
