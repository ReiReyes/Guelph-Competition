import java.io.*;
import java.util.*;

/**
 * Parses routes.csv and stores Route objects by route_id.
 */
public class RouteLoader {
    private Map<String, Route> routes = new HashMap<>();

    public void loadRoutes(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean header = true;

            while ((line = br.readLine()) != null) {
                if (header) { header = false; continue; }

                String[] parts = line.split(",", -1); // -1 keeps empty fields
                if (parts.length >= 9) {
                    String routeId = parts[0];
                    String agencyId = parts[1];
                    String shortName = parts[2];
                    String longName = parts[3];
                    String description = parts[4];
                    int routeType = Integer.parseInt(parts[5]);
                    String routeUrl = parts[6];
                    String routeColor = parts[7];
                    String textColor = parts[8];

                    Route route = new Route(routeId, agencyId, shortName, longName,
                                            description, routeType, routeUrl, routeColor, textColor);
                    routes.put(routeId, route);
                }
            }

            System.out.println("Loaded " + routes.size() + " routes");
        } catch (IOException e) {
            System.err.println("Error loading routes: " + e.getMessage());
        }
    }

    public Route getRoute(String routeId) {
        return routes.get(routeId);
    }

    public Map<String, Route> getRoutes() {
        return routes;
    }
}
