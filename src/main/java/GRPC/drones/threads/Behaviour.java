package GRPC.drones.threads;

public abstract class Behaviour extends Thread{

    protected boolean exit = false;

    public void quit(){
        this.exit = true;
    }

    abstract public void printStatus();
}
