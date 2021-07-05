package MQTT.subscriber;

import GRPC.drone.threads.TBMaster;
import MQTT.message.Delivery;
import org.eclipse.paho.client.mqttv3.*;

import java.util.*;

public class DeliverySubscriber {

    public static final String BROKER = "tcp://localhost:1883";
    public static final String TOPIC = "dronazon/smartcity/orders/";
    public static final int QOS = 2;

    public static MqttClient CLIENT;

    public static ArrayList<Delivery> QUEUE;

    synchronized public boolean queueIsEmpty(){
        for(Delivery d : QUEUE)
            if(!d.isOnProcessing())
                return true;

        return false;
    }

    synchronized  public Delivery HeadDelivery() {
        for(Delivery d : QUEUE)
            if(!d.isOnProcessing())
                return d;
        return null;
    }

    synchronized  public void addDelivery(Delivery d){
        QUEUE.add(d);
    }

    synchronized public void popDelivery(Delivery d) {
        QUEUE.remove(d);
    }

    public void closeConnection() throws MqttException {
        CLIENT.disconnect();
        System.out.println("[ MQTT ] [ CLOSE ]");
    }

    public DeliverySubscriber(){

        QUEUE = new ArrayList<>();

        String clientId = MqttClient.generateClientId();
        try {
            CLIENT = new MqttClient(BROKER, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // Connect the client
            System.out.println("[ MQTT ] "+clientId + " Connecting Broker " + BROKER);
            CLIENT.connect(connOpts);
            System.out.println("[ MQTT ] "+clientId + " Connected");

            // Callback
            CLIENT.setCallback(new MqttCallback() {

                public void messageArrived(String topic, MqttMessage message) {
                    // Called when a message arrives from the server that matches any subscription made by the client

                    Delivery receivedMessage = Delivery.fromJSON(new String(message.getPayload()));
                    System.out.println("[ MQTT ] recived delivery @ "+ receivedMessage.getId());
                    addDelivery(receivedMessage);

                    synchronized (TBMaster.DELIVERY_LOCK){
                        TBMaster.DELIVERY_LOCK.notify();
                    }
                }

                public void connectionLost(Throwable cause) {
                    System.out.println(clientId + " Connectionlost! cause:" + cause.getMessage()+ "-  Thread PID: " + Thread.currentThread().getId());
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not used here
                }

            });
            System.out.println("[ MQTT ] "+clientId + " Subscribing ");
            CLIENT.subscribe(TOPIC,QOS);
            System.out.println("[ MQTT ] "+clientId + " Subscribed to topics : " + TOPIC);

        } catch (MqttException me ) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }

    }
}
