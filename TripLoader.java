import java.io.*;
import java.util.*;

/**
 * Parses trips.csv and stores Trip objects by trip_id.
 */
public class TripLoader {
    private Map<String, Trip> trips = new HashMap<>();

    public void loadTrips(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean header = true;

            while ((line = br.readLine()) != null) {
                if (header) { header = false; continue; }

                String[] parts = line.split(",", -1); // -1 preserves empty fields
                if (parts.length >= 10) {
                    String routeId = parts[0];
                    String serviceId = parts[1];
                    String tripId = parts[2];
                    String headsign = parts[3];
                    String shortName = parts[4];
                    int directionId = parseIntSafe(parts[5]);
                    String blockId = parts[6];
                    String shapeId = parts[7];
                    int wheelchair = parseIntSafe(parts[8]);
                    int bikes = parseIntSafe(parts[9]);

                    Trip trip = new Trip(routeId, serviceId, tripId, headsign, shortName,
                                         directionId, blockId, shapeId, wheelchair, bikes);
                    trips.put(tripId, trip);
                }
            }

            System.out.println("Loaded " + trips.size() + " trips");
        } catch (IOException e) {
            System.err.println("Error loading trips: " + e.getMessage());
        }
    }

    private int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    public Trip getTrip(String tripId) {
        return trips.get(tripId);
    }

    public Map<String, Trip> getTrips() {
        return trips;
    }
}
