import java.io.*;
import java.util.*;

/**
 * Parses stops.csv and stores Stop objects by stop_id.
 */
public class StopLoader {
    private final Map<String, Stop> stops = new HashMap<>();

    public void loadStops(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean header = true;

            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }

                String[] parts = line.split(",", -1); // preserve empty fields
                if (parts.length >= 6) {
                    String stopId = parts[0];
                    String stopCode = parts[1];
                    String stopName = parts[2];
                    String stopDesc = parts[3];
                    double stopLat = parseDoubleSafe(parts[4]);
                    double stopLon = parseDoubleSafe(parts[5]);

                    Stop stop = new Stop(stopId, stopCode, stopName, stopDesc, stopLat, stopLon);
                    stops.put(stopId, stop);
                }
            }

            System.out.println("Loaded " + stops.size() + " stops");
        } catch (IOException e) {
            System.err.println("Error loading stops: " + e.getMessage());
        }
    }

    private double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (Exception e) {
            return -1.0;
        }
    }

    public Stop getStop(String stopId) {
        return stops.get(stopId);
    }

    public Map<String, Stop> getAllStops() {
        return stops;
    }
}
