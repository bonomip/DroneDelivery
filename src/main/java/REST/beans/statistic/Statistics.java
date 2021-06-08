package REST.beans.statistic;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Statistics {
    private ArrayList<Statistic> statList;

    public Statistics(){
        this.statList = new ArrayList<>();
    }

    public Statistics getLast(int n) {
        //todo
        Statistics s = new Statistics();

        for(int i = 0; i < n; i++)
            s.add(new Statistic("debug", i));

        return s;
    }

    public boolean add(Statistic stat) {
        //todo
        this.statList.add(stat);
        return true;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "statList=" + statList +
                '}';
    }
}
