
public class Trip {
    private String routeId;
    private String serviceId;
    private String tripId;
    private String headsign;
    private String shortName;
    private int directionId;
    private String blockId;
    private String shapeId;
    private int wheelchairAccessible;
    private int bikesAllowed;

    public Trip(String routeId, String serviceId, String tripId, String headsign, String shortName,
                int directionId, String blockId, String shapeId, int wheelchairAccessible, int bikesAllowed) {
        this.routeId = routeId;
        this.serviceId = serviceId;
        this.tripId = tripId;
        this.headsign = headsign;
        this.shortName = shortName;
        this.directionId = directionId;
        this.blockId = blockId;
        this.shapeId = shapeId;
        this.wheelchairAccessible = wheelchairAccessible;
        this.bikesAllowed = bikesAllowed;
    }

    public String getRouteId() { return routeId; }
    public String getServiceId() { return serviceId; }
    public String getTripId() { return tripId; }
    public String getHeadsign() { return headsign; }
    public String getShortName() { return shortName; }
    public int getDirectionId() { return directionId; }
    public String getBlockId() { return blockId; }
    public String getShapeId() { return shapeId; }
    public int getWheelchairAccessible() { return wheelchairAccessible; }
    public int getBikesAllowed() { return bikesAllowed; }
}
