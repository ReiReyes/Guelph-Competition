import java.io.*;
import java.util.*;

public class Stop_timesLoader {
    private final Map<String, List<Stop_times>> stop_times = new HashMap<>();

    public void loadStop_times(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean header = true;

            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 10) {
                    String tripId = parts[0];
                    String arrivalTime = parts[1];
                    String departureTime = parts[2];
                    String stopId = parts[3];
                    int stopSequence = Integer.parseInt(parts[4]);
                    int stopHeadsign = Integer.parseInt(parts[5]);
                    int pickupType = Integer.parseInt(parts[6]);
                    int dropOffType = Integer.parseInt(parts[7]);
                    double shapeDistTraveled = Double.parseDouble(parts[8]);
                    int timepoint = Integer.parseInt(parts[9]);

                    Stop_times stop_time = new Stop_times(tripId, arrivalTime, departureTime, stopId, stopSequence,
                            stopHeadsign, pickupType, dropOffType, shapeDistTraveled, timepoint);
                    stop_times.computeIfAbsent(tripId, k -> new ArrayList<>()).add(stop_time);
                }
            }

            // Sort each trip's stop_times by stop sequence
            for (List<Stop_times> times : stop_times.values()) {
                times.sort(Comparator.comparingInt(Stop_times::getStopSequence));
            }

            System.out.println("Loaded " + stop_times.size() + " trips with stop times");
        } catch (IOException e) {
            System.err.println("Error loading stop_times: " + e.getMessage());
        }
    }

    public List<Stop_times> getStop_times(String tripId) {
        return stop_times.getOrDefault(tripId, Collections.emptyList());
    }

    public Map<String, List<Stop_times>> getAllStop_times() {
        return stop_times;
    }
}