package REST.utils;

import REST.AdminServer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataFormatter {

    public static String out(long time){
        DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        return simple.format(new Date(time));
    }
}
