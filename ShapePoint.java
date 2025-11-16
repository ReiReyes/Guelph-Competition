public class ShapePoint {
    private String shapeId;
    private double lat;
    private double lon;
    private int sequence;

    public ShapePoint(String shapeId, double lat, double lon, int sequence) {
        this.shapeId = shapeId;
        this.lat = lat;
        this.lon = lon;
        this.sequence = sequence;
    }

    public String getShapeId() { return this.shapeId; }
    public double getLat() { return this.lat; }
    public double getLon() { return this.lon; }
    public int getSequence() { return this.sequence; }

}
