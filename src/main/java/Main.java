
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        BusStops simulator = new BusStops();

        System.out.println("\n=== RUNNING ALL TESTS ===\n");

        quickManualTests(simulator);

        loopTimeTests(simulator);

        loopRouteTests(simulator);

    
        loopNearbyStopTests(simulator);

        scenarioTests(simulator);

        interactiveConsole(simulator);
    }



    private static void quickManualTests(BusStops sim) {
        System.out.println("\n--- QUICK MANUAL TESTS ---");

        sim.simulateAtTime("12:30:00");
        sim.simulateAtTime("14:00:00");
        sim.simulateAtTime("16:30:00");

        sim.showBusesOnRoute("1");
        sim.showBusesOnRoute("10");

        sim.showNearbyStops(43.545, -80.245, 0.5);
    }

    private static void loopTimeTests(BusStops sim) {
        System.out.println("\n--- TIME LOOP TESTS ---");

        String[] times = {
            "05:00:00", "07:30:00", "09:45:00", "12:00:00",
            "14:00:00", "17:30:00", "21:00:00", "23:55:00"
        };

        for (String t : times) {
            sim.simulateAtTime(t);
        }
    }

    private static void loopRouteTests(BusStops sim) {
        System.out.println("\n--- ROUTE LOOP TESTS ---");

        String[] routes = { "1", "2", "3", "5", "7", "10", "15" };

        for (String r : routes) {
            sim.showBusesOnRoute(r);
        }
    }

    // ============================================================
    // 4️⃣ NEARBY STOP TESTING
    // ============================================================

    private static void loopNearbyStopTests(BusStops sim) {
        System.out.println("\n--- NEARBY STOP TESTS ---");

        double[][] locations = {
            {43.545, -80.245},
            {43.500, -80.230},
            {43.520, -80.250},
            {43.560, -80.230}
        };

        for (double[] L : locations) {
            sim.showNearbyStops(L[0], L[1], 0.5);
        }
    }

    // ============================================================
    // 5️⃣ SCENARIO PRESETS
    // ============================================================

    private static void scenarioTests(BusStops sim) {
        System.out.println("\n--- SCENARIO TESTS ---");

        runMorningScenario(sim);
        runAfternoonScenario(sim);
        runEveningScenario(sim);
    }

    private static void runMorningScenario(BusStops sim) {
        System.out.println("\n*** Morning Scenario ***");
        sim.simulateAtTime("06:00:00");
        sim.simulateAtTime("07:30:00");
        sim.simulateAtTime("09:00:00");
        sim.showBusesOnRoute("7");
    }

    private static void runAfternoonScenario(BusStops sim) {
        System.out.println("\n*** Afternoon Scenario ***");
        sim.simulateAtTime("12:30:00");
        sim.simulateAtTime("14:00:00");
        sim.showNearbyStops(43.545, -80.245, 0.5);
    }

    private static void runEveningScenario(BusStops sim) {
        System.out.println("\n*** Evening Scenario ***");
        sim.simulateAtTime("17:00:00");
        sim.simulateAtTime("19:45:00");
        sim.simulateAtTime("22:00:00");
    }

    // ============================================================
    // 6️⃣ INTERACTIVE CONSOLE
    // ============================================================

    private static void interactiveConsole(BusStops sim) {
        System.out.println("\n--- INTERACTIVE CONSOLE ---");
        System.out.println("Type commands like:");
        System.out.println("  time HH:MM:SS");
        System.out.println("  route SHORTNAME");
        System.out.println("  near LAT LON RADIUS_KM");
        System.out.println("  quit");

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("\n> ");
            String cmd = sc.nextLine().trim();

            if (cmd.equalsIgnoreCase("quit")) {
                System.out.println("Exiting interactive mode.");
                break;
            }

            try {
                if (cmd.startsWith("time ")) {
                    sim.simulateAtTime(cmd.substring(5));
                }
                else if (cmd.startsWith("route ")) {
                    sim.showBusesOnRoute(cmd.substring(6));
                }
                else if (cmd.startsWith("near ")) {
                    String[] p = cmd.split(" ");
                    double lat = Double.parseDouble(p[1]);
                    double lon = Double.parseDouble(p[2]);
                    double radius = Double.parseDouble(p[3]);
                    sim.showNearbyStops(lat, lon, radius);
                }
                else {
                    System.out.println("Commands:");
                    System.out.println("  time HH:MM:SS");
                    System.out.println("  route SHORTNAME");
                    System.out.println("  near LAT LON RADIUS_KM");
                    System.out.println("  quit");
                }
            } catch (Exception e) {
                System.out.println("Invalid command format.");
            }
        }
        sc.close();
    }
}
