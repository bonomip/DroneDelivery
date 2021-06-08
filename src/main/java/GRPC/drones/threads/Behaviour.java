package GRPC.drones.threads;

abstract class Behaviour extends Thread{

    protected boolean exit = false;

    public void quit(){
        this.exit = true;
    }
}
