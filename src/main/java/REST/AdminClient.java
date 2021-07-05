package REST;

import REST.beans.drone.Drone;
import REST.beans.drone.Drones;
import REST.beans.statistic.Statistic;
import REST.beans.statistic.Statistics;
import REST.utils.DataFormatter;
import REST.utils.RESTRequest;
import com.sun.jersey.api.client.Client;
import REST.utils.Uri;

import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminClient {

    private static boolean isIntervalInt(String t1, String t2 ) {
        try{
            return Integer.parseInt(t1) < Integer.parseInt(t2) && isPositiveInt(t1) && isPositiveInt(t2);
        }
        catch (NumberFormatException e){
            return false;
        }
    }

    private static boolean isPositiveInt(String s){
        try {
            return Integer.parseInt(s) >= 0;
        } catch (NumberFormatException e){
            return false;
        }
    }

    private static void printHelp(){
        System.out.println("------- COMMANDS -------");
        System.out.println("---- drones ");
        System.out.println("---- last [INTEGER] ");
        System.out.println("---  del [INTEGER] [INTEGER] ");
        System.out.println("---  km [INTEGER] [INTEGER] ");
        System.out.println("---  help");
        System.out.println("------------------------");
    }

    public static void main(String[] args) throws IOException {
        Client client = Client.create();

        printHelp();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String in;

        while (true) {
            in = br.readLine();

            String[] tokens = in.split(" ");

            switch (tokens[0]){
                case "drones":
                    System.out.println("------- LIST OF DRONES -------");
                    Drones drone_list = getDroneList(client);
                    System.out.println("ID\tIP\t\t\tPORT");
                    for(Drone d : drone_list.getList())
                        System.out.println(d.getId()+"\t"+d.getIp()+"\t"+d.getPort());
                    System.out.println("------------------------------");
                    break;
                case "last":
                    if(tokens.length == 2 && isPositiveInt(tokens[1])) {
                        int n = Integer.parseInt(tokens[1]);
                        System.out.println("------- LAST "+n+" STAT -------");
                        Statistics stat_list = getLastStats(client, n);
                        System.out.println("TIME\t\t\t\t\tDELIVERIES\tMETRES\tPM10\tBATTERY");
                        for(Statistic s : stat_list.getList()){
                            StringBuilder metres = new StringBuilder(Long.toString(s.getMetres()));
                            while(metres.length() < 4)
                                metres.append(" ");

                            System.out.println(
                                    DataFormatter.out(s.getTime())+"\t"+
                                    String.format("%.02f", s.getDeliveries())+"\t\t"+
                                    metres.toString()+"\t"+
                                    String.format("%.02f", s.getPm10() )+"\t"+
                                    String.format("%.02f", s.getBattery())
                            );
                        }
                    } else {
                        System.out.println(" Bad format! Usage << last [INTEGER] >>");
                    }
                    break;
                case "del":
                    if(tokens.length == 3 && isIntervalInt(tokens[1], tokens[2])) {
                        int t1 = Integer.parseInt(tokens[1]);
                        int t2 = Integer.parseInt(tokens[2]);
                        float r = Float.parseFloat(getAvgDeliveries(client, t1, t2));
                        System.out.println("------- AVG DELIVERIES -------");
                        System.out.println("\tBetween minutes "+t1+" and "+t2+" ==> "+String.format("%.02f", r));
                    } else {
                        System.out.println(" Bad format! Usage << del [INTEGER] [INTEGER]>>");
                    }
                    break;
                case "km":
                    if(tokens.length == 3 && isIntervalInt(tokens[1], tokens[2])) {
                        int t1 = Integer.parseInt(tokens[1]);
                        int t2 = Integer.parseInt(tokens[2]);
                        float r = Float.parseFloat(getAvgKm(client, t1, t2));
                        System.out.println("------- AVG KILOMETRES -------");
                        System.out.println("\tBetween minutes "+t1+" and "+t2+" ==> "+String.format("%.02f", r));
                    } else {
                        System.out.println(" Bad format! Usage << km [INTEGER] [INTEGER]>>");
                    }
                    break;
                case "help":
                    printHelp();
                    break;
                default:
                    System.out.println("\nNo command found, use << help >> for"
                            +"a list of available commands");
                    break;
            }


        }
    }

    public static String getAvgKm(Client client, int t1, int t2){
        return RESTRequest.getRequest(client,Uri.AdminServer.InfoService.getAvgKm(t1, t2), MediaType.APPLICATION_JSON)
                .getEntity(String.class);
    }

    public static String getAvgDeliveries(Client client, int t1, int t2){
        return RESTRequest.getRequest(client, Uri.AdminServer.InfoService.getAvgDel(t1,t2), MediaType.APPLICATION_JSON)
                .getEntity(String.class);
    }

    public static Drones getDroneList(Client client){
        return RESTRequest.getRequest(client, Uri.AdminServer.InfoService.getDrones(), MediaType.APPLICATION_JSON)
                .getEntity(Drones.class);
    }

    public static Statistics getLastStats(Client client, int n){
        return RESTRequest.getRequest(client, Uri.AdminServer.InfoService.getLastStats(n), MediaType.APPLICATION_JSON)
                .getEntity(Statistics.class);
    }

}
