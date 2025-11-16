import java.io.*;
import java.util.*;

/**
 * Parses stop_times.csv and organizes stop times by trip_id.
 */
public class Stop_timesLoader {
    private final Map<String, List<Stop_times>> stop_times = new HashMap<>();

    public void loadStop_times(String filename) {
        stop_times.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.err.println("stop_times.csv is empty");
                return;
            }

            String[] header = CSV.parse(headerLine);
            Map<String, Integer> idx = buildIndexMap(header);

            int iTripId      = idx.getOrDefault("trip_id", -1);
            int iArrTime     = idx.getOrDefault("arrival_time", -1);
            int iDepTime     = idx.getOrDefault("departure_time", -1);
            int iStopId      = idx.getOrDefault("stop_id", -1);
            int iStopSeq     = idx.getOrDefault("stop_sequence", -1);
            int iHeadSign    = idx.getOrDefault("stop_headsign", -1);
            int iPickupType  = idx.getOrDefault("pickup_type", -1);
            int iDropOffType = idx.getOrDefault("drop_off_type", -1);
            int iShapeDist   = idx.getOrDefault("shape_dist_traveled", -1);
            int iTimepoint   = idx.getOrDefault("timepoint", -1);

            if (iTripId < 0 || iArrTime < 0 || iDepTime < 0 || iStopId < 0 || iStopSeq < 0) {
                System.err.println("stop_times.csv missing required columns");
                return;
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = CSV.parse(line);
                if (parts.length == 0) continue;

                String tripId        = safeGet(parts, iTripId);
                String arrivalTime   = safeGet(parts, iArrTime);
                String departureTime = safeGet(parts, iDepTime);
                String stopId        = safeGet(parts, iStopId);
                int stopSequence     = parseIntStrict(safeGet(parts, iStopSeq));

                if (tripId.isEmpty() || stopId.isEmpty() || stopSequence < 0) continue;
                if (!isValidGtfsTime(arrivalTime) || !isValidGtfsTime(departureTime)) continue;

                int stopHeadsign = parseIntSafe(safeGet(parts, iHeadSign));
                int pickupType   = parseIntSafe(safeGet(parts, iPickupType));
                int dropOffType  = parseIntSafe(safeGet(parts, iDropOffType));
                double shapeDist = parseDoubleSafe(safeGet(parts, iShapeDist));
                int timepoint    = parseIntSafe(safeGet(parts, iTimepoint));

                Stop_times st = new Stop_times(
                        tripId,
                        arrivalTime,
                        departureTime,
                        stopId,
                        stopSequence,
                        stopHeadsign,
                        pickupType,
                        dropOffType,
                        shapeDist,
                        timepoint
                );

                stop_times.computeIfAbsent(tripId, k -> new ArrayList<>()).add(st);
            }

            // sort each trip by sequence and then by arrival_time
            for (List<Stop_times> list : stop_times.values()) {
                list.sort((a, b) -> {
                    int seq = Integer.compare(a.getStopSequence(), b.getStopSequence());
                    if (seq != 0) return seq;
                    return a.getArrivalTime().compareTo(b.getArrivalTime());
                });
            }

            System.out.println("Loaded " + stop_times.size() + " trips with stop times");
        } catch (IOException e) {
            System.err.println("Error loading stop_times: " + e.getMessage());
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

    private int parseIntStrict(String value) {
        try {
            if (value == null || value.isEmpty()) return -1;
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private int parseIntSafe(String value) {
        try {
            if (value == null || value.isEmpty()) return -1;
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private double parseDoubleSafe(String value) {
        try {
            if (value == null || value.isEmpty()) return Double.NaN;
            return Double.parseDouble(value.trim());
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    // allow GTFS times like 23:59:59 or 24:05:00, 26:10:30 etc.
    private boolean isValidGtfsTime(String t) {
        if (t == null) return false;
        return t.matches("\\d{1,2}:\\d{2}:\\d{2}");
    }

    public List<Stop_times> getStop_times(String tripId) {
        return stop_times.getOrDefault(tripId, Collections.emptyList());
    }

    public Map<String, List<Stop_times>> getAllStop_times() {
        return stop_times;
    }
}
