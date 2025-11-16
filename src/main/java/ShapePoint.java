
public class ShapePoint {
    private String shapeId;
    private double lat;
    private double lon;
    private int sequence;
    private double distanceTravelled;

    public ShapePoint(String shapeId, double lat, double lon, int sequence, double distanceTravelled) {
        this.shapeId = shapeId;
        this.lat = lat;
        this.lon = lon;
        this.sequence = sequence;
        this.distanceTravelled = distanceTravelled;
    }

    public String getShapeId() { return this.shapeId; }
    public double getLat() { return this.lat; }
    public double getLon() { return this.lon; }
    public int getSequence() { return this.sequence; }
    public double getDistanceTravelled() { return this.distanceTravelled; }

}
