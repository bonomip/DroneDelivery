package REST.beans;

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
        return new Statistics();
    }

    public boolean add(Statistic stat) {
        //todo
        this.statList.add(stat);
        return true;
    }
}
