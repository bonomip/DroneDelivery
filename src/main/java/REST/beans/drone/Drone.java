package REST.beans.drone;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Drone {

    private int id;
    private String ip;
    private int port;

    public Drone(){}

    public Drone(int id, String ip, int port){
        this.id = id;
        this.ip = ip;
        this.port = port;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getIp(){
        return this.ip;
    }

    public void setIp(String ip){
        this.ip = ip;
    }

    public int getPort(){
        return this.port;
    }

    public void setPort(int port){ this.port = port; }

    @Override
    public String toString() {
        return "{\"id\":"+this.id+",\"ip\":\""+this.ip+"\",\"port\":"+this.port+"}";
    }
}
