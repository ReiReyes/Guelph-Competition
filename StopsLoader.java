import java.io.*;
import java.util.*;

public class StopsLoader {
    private final Map<String, List<Stops>> stops = new HashMap<>();
    public void loadStops(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean header = true;

            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String stopId = parts[0];
                    String stopCode = parts[1];
                    String stopName = parts[2];
                    String stopDesc = parts[3];
                    double stopLat = Double.parseDouble(parts[4]);
                    double stopLon = Double.parseDouble(parts[5]);

                    Stops stop = new Stops(stopId, stopCode, stopName, stopDesc, stopLat, stopLon);
                    stops.computeIfAbsent(stopId, k -> new ArrayList<>()).add(stop);
                }
            }

            System.out.println("Loaded " + stops.size() + " stops");
        } catch (IOException e) {
            System.err.println("Error loading stops: " + e.getMessage());
        }
    }

    public List<Stops> getStops(String stopId) {
        return stops.getOrDefault(stopId, Collections.emptyList());
    }

    public Map<String, List<Stops>> getAllStops() {
        return stops;
    }
}