
import java.time.LocalTime;
import java.util.*;

/**
 * GTFS-based bus simulation engine.
 */
public class BusStops {

    private final Map<String, Route> routes;
    private final Map<String, Stop> stops;
    private final List<Bus> buses;

    public BusStops() {

        // Load routes
        RouteLoader routeLoader = new RouteLoader();
        routeLoader.loadRoutes("csv_files/routes.csv");
        routes = routeLoader.getRoutes();

        // Load stops
        StopLoader stopLoader = new StopLoader();
        stopLoader.loadStops("csv_files/stops.csv");
        stops = stopLoader.getAllStops();

        // Load stop_times and create buses
        buses = new ArrayList<>();
        loadStopTimes("csv_files/stop_times.csv");
    }

    /**
     * Load stop_times.csv and create up to 100 buses
     */
    private void loadStopTimes(String filename) {
        Stop_timesLoader loader = new Stop_timesLoader();
        loader.loadStop_times(filename);
        Map<String, List<Stop_times>> tripStops = loader.getAllStop_times();

        int busCount = 0;
        for (Map.Entry<String, List<Stop_times>> entry : tripStops.entrySet()) {
            if (busCount >= 100) break;

            List<StopTime> route = new ArrayList<>();
            for (Stop_times st : entry.getValue()) {
                route.add(new StopTime(
                        st.getTripId(),
                        st.getArrivalTime(),
                        st.getDepartureTime(),
                        st.getStopId(),
                        st.getStopSequence()
                ));
            }

            // Ensure correct ordering
            route.sort(Comparator.naturalOrder());

            buses.add(new Bus("BUS_" + busCount, route));
            busCount++;
        }

        System.out.println("Created " + buses.size() + " buses for simulation");
    }

    /**
     * Show buses currently at a stop
     */
    public void simulateAtTime(String timeStr) {
        System.out.println("\n=== Bus Simulation at " + timeStr + " ===");
        int active = 0;

        for (Bus bus : buses) {
            Stop stop = bus.getStopAtTime(timeStr, stops);
            if (stop != null) {
                System.out.println(bus.getId() + " at " + stop.getStopName());
                active++;
            }
        }

        System.out.println("Total buses active: " + active);
    }

    /**
     * Show route metadata
     */
    public void showBusesOnRoute(String routeShortName) {
        System.out.println("\n=== Buses on Route: " + routeShortName + " ===");

        Route route = routes.values().stream()
                .filter(r -> r.getShortName() != null &&
                             r.getShortName().equals(routeShortName))
                .findFirst()
                .orElse(null);

        if (route != null) {
            System.out.println("Route: " + route.getLongName());
            System.out.println("Color: #" + route.getRouteColor());
        } else {
            System.out.println("Route not found");
        }
    }

    /**
     * Show stops near (lat, lon) within radius
     */
    public void showNearbyStops(double lat, double lon, double radiusKm) {
        System.out.println("\n=== Stops within " + radiusKm + " km ===");
        int count = 0;

        for (Stop stop : stops.values()) {
            double dist = calculateDistance(lat, lon, stop.getStopLat(), stop.getStopLon());
            if (dist <= radiusKm) {
                System.out.println(stop.getStopName() + " (" + String.format("%.2f", dist) + " km)");
                count++;
            }
        }

        System.out.println("Found " + count + " stops");
    }

    /**
     * Haversine formula
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    // ==========================================================
    // === Inner Classes ========================================
    // ==========================================================

    private static class StopTime implements Comparable<StopTime> {
        private final String tripId;
        private final String arrivalTime;
        private final String departureTime;
        private final String stopId;
        private final int stopSequence;

        StopTime(String tripId, String arrivalTime, String departureTime,
                 String stopId, int stopSequence) {
            this.tripId = tripId;
            this.arrivalTime = arrivalTime;
            this.departureTime = departureTime;
            this.stopId = stopId;
            this.stopSequence = stopSequence;
        }

        @Override
        public int compareTo(StopTime other) {
            return Integer.compare(this.stopSequence, other.stopSequence);
        }

        public String getArrivalTime() { return arrivalTime; }
        public String getDepartureTime() { return departureTime; }
        public String getStopId() { return stopId; }
    }

    // ==========================================================
    // === Bus class with GTFS-safe time parsing ================
    // ==========================================================

    private static class Bus {
        private final String id;
        private final List<StopTime> route;

        Bus(String id, List<StopTime> route) {
            this.id = id;
            this.route = route;
        }

        public String getId() { return id; }

        /**
         * GTFS-safe time parser:
         *  Supports:
         *    - "9:45:00"
         *    - "07:12:30"
         *    - "24:10:00"
         *    - "26:25:30"
         */
        private LocalTime parseGtfsTime(String t) {
            if (t == null || t.isEmpty()) return null;

            String[] p = t.split(":");
            if (p.length < 3) return null;

            int hour = Integer.parseInt(p[0]);   // 0–99 allowed
            int min  = Integer.parseInt(p[1]);
            int sec  = Integer.parseInt(p[2]);

            // LocalTime supports 0–23; wrap anything above
            hour = hour % 24;

            return LocalTime.of(hour, min, sec);
        }

        public Stop getStopAtTime(String timeStr, Map<String, Stop> stops) {
            LocalTime check = parseGtfsTime(timeStr);
            if (check == null) return null;

            for (StopTime st : route) {
                LocalTime arrival = parseGtfsTime(st.getArrivalTime());
                LocalTime departure = parseGtfsTime(st.getDepartureTime());

                if (arrival == null || departure == null) continue;

                // check ∈ [arrival, departure]
                if (!check.isBefore(arrival) && !check.isAfter(departure)) {
                    return stops.get(st.getStopId());
                }
            }
            return null;
        }
    }
}
