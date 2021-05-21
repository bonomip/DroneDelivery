package utils;

public class Uri {

    public static class AdminServer {
        public static final int PORT = 1337;
        public static final String HOST = "localhost";

        public static final String SERVER_URL = "http://"+HOST+":"+PORT+"/";

        public static class InfoService{
            public static final String INFO_SERVICE = "info";

            public static final String AVG_DEL = "/avg-delivery/";
            public static final String AVG_KM = "/avg-km/";
            public static final String LAST_STATS = "/last-stats/";
            public static final String DRONES = "/drones";

            //produces json drone
            public static String getDrones(){
                return SERVER_URL+INFO_SERVICE+DRONES;
            }

            public static String getLastStats(int n){
                return SERVER_URL+INFO_SERVICE+LAST_STATS+n;
            }

            public static String getAvgKm(int t1, int t2){
                return SERVER_URL+INFO_SERVICE+AVG_KM+t1+"/"+t2;
            }

            public static String getAvgDel(int t1, int t2){
                return SERVER_URL+INFO_SERVICE+AVG_DEL+t1+"/"+t2;
            }
        }

        public static class DroneService{

            public static final String DRONE_SERVICE = "drones";

            public static String postDrone(){
                return SERVER_URL+DRONE_SERVICE;
            }

            public static String deleteDrone(){
                return SERVER_URL+DRONE_SERVICE;
            }

        }

        //todo shortcut for each method for services
    }


}
