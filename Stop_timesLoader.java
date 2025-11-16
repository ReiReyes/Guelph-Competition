import java.io.*;
import java.util.*;

/**
 * Parses stop_times.csv and organizes stop times by trip_id.
 */
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

                String[] parts = line.split(",", -1); // preserve empty fields
                if (parts.length >= 10) {
                    String tripId = parts[0];
                    String arrivalTime = parts[1];
                    String departureTime = parts[2];
                    String stopId = parts[3];
                    int stopSequence = parseIntSafe(parts[4]);
                    int stopHeadsign = parseIntSafe(parts[5]);
                    int pickupType = parseIntSafe(parts[6]);
                    int dropOffType = parseIntSafe(parts[7]);
                    double shapeDistTraveled = parseDoubleSafe(parts[8]);
                    int timepoint = parseIntSafe(parts[9]);

                    Stop_times stopTime = new Stop_times(tripId, arrivalTime, departureTime, stopId, stopSequence,
                            stopHeadsign, pickupType, dropOffType, shapeDistTraveled, timepoint);
                    stop_times.computeIfAbsent(tripId, k -> new ArrayList<>()).add(stopTime);
                }
            }

            for (List<Stop_times> times : stop_times.values()) {
                times.sort(Comparator.comparingInt(Stop_times::getStopSequence));
            }

            System.out.println("Loaded " + stop_times.size() + " trips with stop times");
        } catch (IOException e) {
            System.err.println("Error loading stop_times: " + e.getMessage());
        }
    }

    private int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (Exception e) {
            return -1.0;
        }
    }

    public List<Stop_times> getStop_times(String tripId) {
        return stop_times.getOrDefault(tripId, Collections.emptyList());
    }

    public Map<String, List<Stop_times>> getAllStop_times() {
        return stop_times;
    }
}
