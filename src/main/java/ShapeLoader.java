
import java.io.*;
import java.util.*;

/**
 * Parses shapes.csv and stores shape points for each shape_id.
 */
public class ShapeLoader {
    private final Map<String, List<ShapePoint>> shapes = new HashMap<>();

    /**
     * Load shape points from shapes.csv.
     * Expected GTFS columns: shape_id,shape_pt_lat,shape_pt_lon,shape_pt_sequence,shape_dist_traveled
     */
    public void loadShapes(String filename) {
        shapes.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.err.println("shapes.csv is empty");
                return;
            }

            String[] header = CSV.parse(headerLine);
            Map<String, Integer> idx = buildIndexMap(header);

            int iShapeId   = idx.getOrDefault("shape_id", -1);
            int iLat       = idx.getOrDefault("shape_pt_lat", -1);
            int iLon       = idx.getOrDefault("shape_pt_lon", -1);
            int iSeq       = idx.getOrDefault("shape_pt_sequence", -1);
            int iDist      = idx.getOrDefault("shape_dist_traveled", -1);
            if (iDist < 0) {
                // some feeds use a slightly different name
                iDist = idx.getOrDefault("distance_travelled", -1);
            }

            if (iShapeId < 0 || iLat < 0 || iLon < 0 || iSeq < 0) {
                System.err.println("shapes.csv missing required columns");
                return;
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = CSV.parse(line);
                if (parts.length == 0) continue;

                String shapeId = safeGet(parts, iShapeId);
                if (shapeId.isEmpty()) continue;

                double lat = parseDoubleSafe(safeGet(parts, iLat));
                double lon = parseDoubleSafe(safeGet(parts, iLon));
                int seq    = parseIntSafe(safeGet(parts, iSeq));
                double dist= parseDoubleSafe(safeGet(parts, iDist));

                if (Double.isNaN(lat) || Double.isNaN(lon) || seq < 0) continue;

                ShapePoint point = new ShapePoint(shapeId, lat, lon, seq, dist);
                shapes.computeIfAbsent(shapeId, k -> new ArrayList<>()).add(point);
            }

            // sort each shape's points by sequence
            for (List<ShapePoint> points : shapes.values()) {
                points.sort(Comparator.comparingInt(ShapePoint::getSequence));
            }

            System.out.println("Loaded " + shapes.size() + " shapes");
        } catch (IOException e) {
            System.err.println("Error loading shapes: " + e.getMessage());
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

    private double parseDoubleSafe(String value) {
        try {
            if (value == null || value.isEmpty()) return Double.NaN;
            return Double.parseDouble(value.trim());
        } catch (Exception e) {
            return Double.NaN;
        }
    }

    /**
     * Get all shape points for a given shape_id
     */
    public List<ShapePoint> getShape(String shapeId) {
        return shapes.getOrDefault(shapeId, Collections.emptyList());
    }

    /**
     * Get all shapes
     */
    public Map<String, List<ShapePoint>> getShapes() {
        return shapes;
    }
}
