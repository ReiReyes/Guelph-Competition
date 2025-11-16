import java.io.*;
import java.util.*;

public class BusStops {
    
    // Data structures for bus system
    private Map<String, Route> routes;
    private Map<Integer, Stop> stops;
    private List<Bus> buses;
    
    public BusStops() {
        routes = new HashMap<>();
        stops = new HashMap<>();
        buses = new ArrayList<>();
        loadData();
    }
    
    /**
     * Load routes and stops data from CSV files
     */
    private void loadData() {
        loadRoutes(".csv files/routes.csv");
        loadStops(".csv files/stops.csv");
        loadStopTimes(".csv files/stop_times.csv");
    }
    
    /**
     * Load routes from routes.csv
     */
    private void loadRoutes(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean header = true;
            
            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }
                
                String[] parts = parseCSV(line);
                if (parts.length >= 4) {
                    String routeId = parts[0];
                    String routeName = parts[2];
                    String routeLongName = parts[3];
                    String color = parts[7];
                    
                    Route route = new Route(routeId, routeName, routeLongName, color);
                    routes.put(routeId, route);
                }
            }
            System.out.println("Loaded " + routes.size() + " routes");
        } catch (IOException e) {
            System.err.println("Error loading routes: " + e.getMessage());
        }
    }
    
    /**
     * Load stops from stops.csv
     */
    private void loadStops(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean header = true;
            
            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }
                
                String[] parts = parseCSV(line);
                if (parts.length >= 6) {
                    int stopId = Integer.parseInt(parts[0]);
                    String stopName = parts[2];
                    double latitude = Double.parseDouble(parts[4]);
                    double longitude = Double.parseDouble(parts[5]);
                    
                    Stop stop = new Stop(stopId, stopName, latitude, longitude);
                    stops.put(stopId, stop);
                }
            }
            System.out.println("Loaded " + stops.size() + " stops");
        } catch (IOException e) {
            System.err.println("Error loading stops: " + e.getMessage());
        }
    }
    
    /**
     * Load stop times and create buses with routes
     */
    private void loadStopTimes(String filename) {
        Map<String, List<StopTime>> tripStops = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean header = true;
            
            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }
                
                String[] parts = parseCSV(line);
                if (parts.length >= 5) {
                    String tripId = parts[0];
                    String arrivalTime = parts[1];
                    String departureTime = parts[2];
                    int stopId = Integer.parseInt(parts[3]);
                    int stopSequence = Integer.parseInt(parts[4]);
                    
                    StopTime stopTime = new StopTime(tripId, arrivalTime, departureTime, stopId, stopSequence);
                    tripStops.computeIfAbsent(tripId, k -> new ArrayList<>()).add(stopTime);
                }
            }
            
            System.out.println("Loaded " + tripStops.size() + " trips");
            
            // Create buses for each unique trip (limited to prevent memory issues)
            int busCount = 0;
            for (Map.Entry<String, List<StopTime>> entry : tripStops.entrySet()) {
                if (busCount >= 100) break; // Limit to 100 buses for demo
                
                List<StopTime> stopTimes = entry.getValue();
                Collections.sort(stopTimes);
                
                Bus bus = new Bus("BUS_" + busCount, stopTimes);
                buses.add(bus);
                busCount++;
            }
            
            System.out.println("Created " + buses.size() + " buses for simulation");
        } catch (IOException e) {
            System.err.println("Error loading stop times: " + e.getMessage());
        }
    }
    
    /**
     * Simulate buses moving through stops at a given time
     */
    public void simulateAtTime(String timeStr) {
        System.out.println("\n=== Bus Simulation at " + timeStr + " ===");
        
        int busesAtStops = 0;
        for (Bus bus : buses) {
            Stop currentStop = bus.getStopAtTime(timeStr, stops);
            if (currentStop != null) {
                System.out.println(bus.getId() + " at " + currentStop.getName());
                busesAtStops++;
            }
        }
        System.out.println("Total buses active: " + busesAtStops);
    }
    
    /**
     * Get all buses on a specific route
     */
    public void showBusesOnRoute(String routeName) {
        System.out.println("\n=== Buses on Route: " + routeName + " ===");
        
        Route route = routes.values().stream()
            .filter(r -> r.getShortName().equals(routeName))
            .findFirst()
            .orElse(null);
            
        if (route != null) {
            System.out.println("Route: " + route.getLongName());
            System.out.println("Color: #" + route.getColor());
        } else {
            System.out.println("Route not found");
        }
    }
    
    /**
     * Show information about stops near a location
     */
    public void showNearbyStops(double latitude, double longitude, double radiusKm) {
        System.out.println("\n=== Stops within " + radiusKm + " km ===");
        
        int count = 0;
        for (Stop stop : stops.values()) {
            double distance = calculateDistance(latitude, longitude, stop.getLatitude(), stop.getLongitude());
            if (distance <= radiusKm) {
                System.out.println(stop.getName() + " (" + String.format("%.2f", distance) + " km)");
                count++;
            }
        }
        System.out.println("Found " + count + " stops");
    }
    
    /**
     * Calculate distance between two coordinates in km
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
    
    /**
     * Parse CSV line handling quoted fields
     */
    private String[] parseCSV(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString().trim());
        
        return result.toArray(new String[0]);
    }
    
    // Inner classes
    
    /**
     * Represents a bus route
     */
    private static class Route {
        private String id;
        private String shortName;
        private String longName;
        private String color;
        
        Route(String id, String shortName, String longName, String color) {
            this.id = id;
            this.shortName = shortName;
            this.longName = longName;
            this.color = color;
        }
        
        String getId() { return id; }
        String getShortName() { return shortName; }
        String getLongName() { return longName; }
        String getColor() { return color; }
    }
    
    /**
     * Represents a bus stop
     */
    public static class Stop {
        private int id;
        private String name;
        private double latitude;
        private double longitude;
        
        Stop(int id, String name, double latitude, double longitude) {
            this.id = id;
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
        }
        
        int getId() { return id; }
        String getName() { return name; }
        double getLatitude() { return latitude; }
        double getLongitude() { return longitude; }
    }
    
    /**
     * Represents a stop time for a trip
     */
    private static class StopTime implements Comparable<StopTime> {
        private String tripId;
        private String arrivalTime;
        private String departureTime;
        private int stopId;
        private int stopSequence;
        
        StopTime(String tripId, String arrivalTime, String departureTime, int stopId, int stopSequence) {
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
        
        String getArrivalTime() { return arrivalTime; }
        String getDepartureTime() { return departureTime; }
        int getStopId() { return stopId; }
    }
    
    /**
     * Represents a bus in the simulation
     */
    private static class Bus {
        private String id;
        private List<StopTime> route;
        
        Bus(String id, List<StopTime> route) {
            this.id = id;
            this.route = route;
        }
        
        String getId() { return id; }
        
        /**
         * Get the stop where this bus is at a given time
         */
        Stop getStopAtTime(String timeStr, Map<Integer, Stop> stops) {
            for (StopTime st : route) {
                if (isTimeBetween(timeStr, st.getArrivalTime(), st.getDepartureTime())) {
                    return stops.get(st.getStopId());
                }
            }
            return null;
        }
        
        /**
         * Check if a time falls between arrival and departure
         */
        private boolean isTimeBetween(String checkTime, String arrival, String departure) {
            return checkTime.compareTo(arrival) >= 0 && checkTime.compareTo(departure) <= 0;
        }
    }
    
    // Main method for testing
    public static void main(String[] args) {
        BusStops simulator = new BusStops();
        
        // Simulate buses at specific times
        simulator.simulateAtTime("12:30:00");
        simulator.simulateAtTime("14:00:00");
        simulator.simulateAtTime("16:30:00");
        
        // Show route information
        simulator.showBusesOnRoute("1");
        simulator.showBusesOnRoute("10");
        
        // Show nearby stops (near downtown Guelph coordinates)
        simulator.showNearbyStops(43.545, -80.245, 0.5);
    }
}
