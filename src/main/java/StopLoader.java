
import java.io.*;
import java.util.*;

/**
 * Parses stops.csv and stores Stop objects by stop_id.
 */
public class StopLoader {
    private final Map<String, Stop> stops = new HashMap<>();

    public void loadStops(String filename) {
        stops.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.err.println("stops.csv is empty");
                return;
            }

            String[] header = CSV.parse(headerLine);
            Map<String, Integer> idx = buildIndexMap(header);

            int iStopId  = idx.getOrDefault("stop_id", -1);
            int iStopCode= idx.getOrDefault("stop_code", -1);
            int iStopName= idx.getOrDefault("stop_name", -1);
            int iStopDesc= idx.getOrDefault("stop_desc", -1);
            int iStopLat = idx.getOrDefault("stop_lat", -1);
            int iStopLon = idx.getOrDefault("stop_lon", -1);

            if (iStopId < 0 || iStopLat < 0 || iStopLon < 0) {
                System.err.println("stops.csv missing required columns (stop_id, stop_lat, stop_lon)");
                return;
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = CSV.parse(line);
                if (parts.length == 0) continue;

                String stopId   = safeGet(parts, iStopId);
                String stopCode = safeGet(parts, iStopCode);
                String stopName = safeGet(parts, iStopName);
                String stopDesc = safeGet(parts, iStopDesc);
                double stopLat  = parseDoubleSafe(safeGet(parts, iStopLat));
                double stopLon  = parseDoubleSafe(safeGet(parts, iStopLon));

                if (stopId.isEmpty() || Double.isNaN(stopLat) || Double.isNaN(stopLon)) {
                    // skip malformed rows
                    continue;
                }

                Stop stop = new Stop(stopId, stopCode, stopName, stopDesc, stopLat, stopLon);
                stops.put(stopId, stop);
            }

            System.out.println("Loaded " + stops.size() + " stops");
        } catch (IOException e) {
            System.err.println("Error loading stops: " + e.getMessage());
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

    private double parseDoubleSafe(String value) {
        try {
            if (value == null || value.isEmpty()) return Double.NaN;
            return Double.parseDouble(value.trim());
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    public Stop getStop(String stopId) {
        return stops.get(stopId);
    }

    public Map<String, Stop> getAllStops() {
        return stops;
    }
}
