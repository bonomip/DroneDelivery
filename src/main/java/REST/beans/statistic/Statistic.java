package REST.beans.statistic;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Statistic {

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    private int timestamp;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    private String info;

    public Statistic(){}

    public Statistic(String info, int time){
        this.info = info;
        this.timestamp = time;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "timestamp=" + timestamp +
                ", info='" + info + '\'' +
                '}';
    }
}
