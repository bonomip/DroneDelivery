package SENSOR;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PM10Buffer implements Buffer {

    static int COUNT = 0;
    static int ID = 2;
    ArrayList<Measurement> list;
    ArrayList<Measurement> window;

    public PM10Buffer(){
        this.list = new ArrayList<>();
        this.window = new ArrayList<>();
    }

    private void calculateAvg(){
        long t = 0;
        double v = 0;

        for(int i = 0; i < 12; i++)
        {
            t += this.list.get(i).getTimestamp();
            v += this.list.get(i).getValue();
        }

        this.list.subList(0, 8).clear();

        this.window.add(
                new Measurement("pm10-"+(ID++),
                        "PM10",
                        v/((double)12),
                        t/12)
                );
    }

    @Override
    public synchronized void addMeasurement(Measurement m) {
        this.list.add(m);
        if(++COUNT == 12)
        {
            calculateAvg();
            COUNT = 4;
        }
    }

    @Override
    public synchronized List<Measurement> readAllAndClean() {
        ArrayList<Measurement> result = new ArrayList<>(this.window);
        this.window.clear();
        return result;
    }

    public static List<Double> fromMeasurementToDouble(List<Measurement> measurement_list){
        return measurement_list.stream()
                .map(Measurement::getValue)
                .collect(Collectors.toList());
    }
}
