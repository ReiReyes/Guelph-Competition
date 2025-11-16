import java.io.*;
import java.util.*;

/**
 * Parses shapes.csv and stores shape points for each shape_id.
 */
public class ShapeLoader {
    private Map<String, List<ShapePoint>> shapes = new HashMap<>();

    /**
     * Load shape points from shapes.csv
     * Expected format:
     * shape_id,shape_pt_lat,shape_pt_lon,shape_pt_sequence,distance_travelled
     */
    public void loadShapes(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean header = true;

            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String shapeId = parts[0];
                    double lat = Double.parseDouble(parts[1]);
                    double lon = Double.parseDouble(parts[2]);
                    int sequence = Integer.parseInt(parts[3]);
                    double distance = Double.parseDouble(parts[4]);

                    ShapePoint point = new ShapePoint(shapeId, lat, lon, sequence, distance);
                    shapes.computeIfAbsent(shapeId, k -> new ArrayList<>()).add(point);
                }
            }

            // Sort each shape's points by sequence
            for (List<ShapePoint> points : shapes.values()) {
                points.sort(Comparator.comparingInt(ShapePoint::getSequence));
            }

            System.out.println("Loaded " + shapes.size() + " shapes");
        } catch (IOException e) {
            System.err.println("Error loading shapes: " + e.getMessage());
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
