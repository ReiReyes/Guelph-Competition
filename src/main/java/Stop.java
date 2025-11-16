
public class Stop {
    private String stopId;
    private String stopCode;
    private String stopName;
    private String stopDesc;
    private double stopLat;
    private double stopLon;

    public Stop(String stopId, String stopCode, String stopName, String stopDesc, double stopLat, double stopLon) {
        this.stopId = stopId;
        this.stopCode = stopCode;
        this.stopName = stopName;
        this.stopDesc = stopDesc;
        this.stopLat = stopLat;
        this.stopLon = stopLon;
    }

    public String getStopId() { return stopId; }
    public String getStopCode() { return stopCode; }
    public String getStopName() { return stopName; }
    public String getStopDesc() { return stopDesc; }
    public double getStopLat() { return stopLat; }
    public double getStopLon() { return stopLon; }
}
