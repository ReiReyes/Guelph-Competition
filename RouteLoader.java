import java.io.*;
import java.util.*;

/**
 * Parses routes.csv and stores Route objects by route_id.
 */
public class RouteLoader {
    private final Map<String, Route> routes = new HashMap<>();

    public void loadRoutes(String filename) {
        routes.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.err.println("routes.csv is empty");
                return;
            }

            String[] header = CSV.parse(headerLine);
            Map<String, Integer> idx = buildIndexMap(header);

            int iRouteId   = idx.getOrDefault("route_id", -1);
            int iAgencyId  = idx.getOrDefault("agency_id", -1);
            int iShortName = idx.getOrDefault("route_short_name", -1);
            int iLongName  = idx.getOrDefault("route_long_name", -1);
            int iDesc      = idx.getOrDefault("route_desc", -1);
            int iRouteType = idx.getOrDefault("route_type", -1);
            int iRouteUrl  = idx.getOrDefault("route_url", -1);
            int iColor     = idx.getOrDefault("route_color", -1);
            int iTextColor = idx.getOrDefault("route_text_color", -1);

            if (iRouteId < 0 || iRouteType < 0) {
                System.err.println("routes.csv missing required columns");
                return;
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = CSV.parse(line);
                if (parts.length == 0) continue;

                String routeId  = safeGet(parts, iRouteId);
                if (routeId.isEmpty()) continue;

                String agencyId = safeGet(parts, iAgencyId);
                String shortName= safeGet(parts, iShortName);
                String longName = safeGet(parts, iLongName);
                String desc     = safeGet(parts, iDesc);
                int routeType   = parseIntSafe(safeGet(parts, iRouteType));
                String routeUrl = safeGet(parts, iRouteUrl);
                String routeColor = safeGet(parts, iColor);
                String textColor  = safeGet(parts, iTextColor);

                Route route = new Route(routeId, agencyId, shortName, longName,
                        desc, routeType, routeUrl, routeColor, textColor);

                routes.put(routeId, route);
            }

            System.out.println("Loaded " + routes.size() + " routes");
        } catch (IOException e) {
            System.err.println("Error loading routes: " + e.getMessage());
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

    public Route getRoute(String routeId) {
        return routes.get(routeId);
    }

    public Map<String, Route> getRoutes() {
        return routes;
    }
}
