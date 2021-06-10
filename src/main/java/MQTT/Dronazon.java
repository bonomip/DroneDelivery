package MQTT;

import MQTT.message.Delivery;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Dronazon {

    public static final String BROKER = "tcp://localhost:1883";
    public static final String TOPIC = "dronazon/smartcity/orders/";
    public static final int QOS = 2;

    public static void main(String[] args) throws IOException, InterruptedException {

        startMosquitto();

        MqttClient client;
        String clientId = MqttClient.generateClientId();

        try {
            client = new MqttClient(BROKER, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            System.out.println("[ MQTT ] "+clientId + " Connecting Broker " + BROKER);
            client.connect(connOpts);
            System.out.println("[ MQTT ] "+clientId + " Connected");

            while(true){ //sending loop
                Delivery delivery = createDelivery();
                MqttMessage message = new MqttMessage(delivery.toJSON().getBytes());
                message.setQos(QOS);
                System.out.println("[ MQTT ] "+clientId + " Publishing message: "+ delivery.getId());
                client.publish(TOPIC, message);
                System.out.println("[ MQTT ] "+clientId + " Message published");
                Thread.sleep(5000);
            }
        } catch (MqttException | InterruptedException me ) {
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }


    }

    public static int count = 0;

    public static Delivery createDelivery(){
        int[] origin = createPosition();
        int[] destination = createPosition(origin);

        return new Delivery(count++, origin, destination);
    }

    public static int[] createPosition(int[] origin){
        int x = (int)(Math.random()*10); //0-9
        x = origin[0] == x ? (x+1)%10 : x; //to exclude origin == destination
        int y = (int)(Math.random()*10); //0-9

        return new int[]{x,y};
    }

    public static int[] createPosition(){
        int x = (int)(Math.random()*10); //0-9
        int y = (int)(Math.random()*10); //0-9

        return new int[]{x,y};
    }

    public static void startMosquitto() throws IOException, InterruptedException {
        String command = "brew services start mosquitto";

        Process proc = Runtime.getRuntime().exec(command);

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(proc.getInputStream()));

        String line = "";
        while((line = reader.readLine()) != null) {
            System.out.print(line + "\n");
        }

        Thread.sleep(5000);
    }

    public static void stopMosquitto() throws IOException {
        String command = "brew services stop mosquitto";

        Process proc = Runtime.getRuntime().exec(command);
    }


}
