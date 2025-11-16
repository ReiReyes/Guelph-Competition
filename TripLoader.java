import java.io.*;
import java.util.*;

/**
 * Parses trips.csv and stores Trip objects by trip_id.
 */
public class TripLoader {
    private final Map<String, Trip> trips = new HashMap<>();

    public void loadTrips(String filename) {
        trips.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.err.println("trips.csv is empty");
                return;
            }

            String[] header = CSV.parse(headerLine);
            Map<String, Integer> idx = buildIndexMap(header);

            int iRouteId   = idx.getOrDefault("route_id", -1);
            int iServiceId = idx.getOrDefault("service_id", -1);
            int iTripId    = idx.getOrDefault("trip_id", -1);
            int iHeadSign  = idx.getOrDefault("trip_headsign", -1);
            int iShortName = idx.getOrDefault("trip_short_name", -1);
            int iDirection = idx.getOrDefault("direction_id", -1);
            int iBlockId   = idx.getOrDefault("block_id", -1);
            int iShapeId   = idx.getOrDefault("shape_id", -1);
            int iWheelchair= idx.getOrDefault("wheelchair_accessible", -1);
            int iBikes     = idx.getOrDefault("bikes_allowed", -1);

            if (iRouteId < 0 || iTripId < 0 || iServiceId < 0) {
                System.err.println("trips.csv missing required columns");
                return;
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = CSV.parse(line);
                if (parts.length == 0) continue;

                String routeId  = safeGet(parts, iRouteId);
                String serviceId= safeGet(parts, iServiceId);
                String tripId   = safeGet(parts, iTripId);

                if (routeId.isEmpty() || tripId.isEmpty()) continue;

                String headsign = safeGet(parts, iHeadSign);
                String shortName= safeGet(parts, iShortName);
                int directionId = parseIntSafe(safeGet(parts, iDirection));
                String blockId  = safeGet(parts, iBlockId);
                String shapeId  = safeGet(parts, iShapeId);
                int wheelchair  = parseIntSafe(safeGet(parts, iWheelchair));
                int bikes       = parseIntSafe(safeGet(parts, iBikes));

                Trip trip = new Trip(routeId, serviceId, tripId, headsign, shortName,
                        directionId, blockId, shapeId, wheelchair, bikes);

                trips.put(tripId, trip);
            }

            System.out.println("Loaded " + trips.size() + " trips");
        } catch (IOException e) {
            System.err.println("Error loading trips: " + e.getMessage());
        }
    }

    private Map<String, Integer> buildIndexMap(String[] header) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < header.length; i++) {
            map.put(header[i].trim().toLowerCase(), i);
        }
        return map;
    }

    private String safeGet(String[] arr, int idx) {
        if (idx < 0 || idx >= arr.length) return "";
        return arr[idx].trim();
    }

    private int parseIntSafe(String value) {
        try {
            if (value == null || value.isEmpty()) return -1;
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
