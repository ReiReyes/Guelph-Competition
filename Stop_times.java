public class Stop_times {
    private String tripId;
    private String arrivalTime;
    private String departureTime;
    private String stopId;
    private int stopSequence;
    private int stopHeadsign;
    private int pickupType;
    private int dropOffType;
    private double shapeDistTraveled;
    private int timepoint;

    public Stop_times(String tripId, String arrivalTime, String departureTime, String stopId, int stopSequence,
                      int stopHeadsign, int pickupType, int dropOffType, double shapeDistTraveled, int timepoint) {
        this.tripId = tripId;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.stopId = stopId;
        this.stopSequence = stopSequence;
        this.stopHeadsign = stopHeadsign;
        this.pickupType = pickupType;
        this.dropOffType = dropOffType;
        this.shapeDistTraveled = shapeDistTraveled;
        this.timepoint = timepoint;
    }

    public String getTripId() { return tripId; }
    public String getArrivalTime() { return arrivalTime; }
    public String getDepartureTime() { return departureTime; }
    public String getStopId() { return stopId; }
    public int getStopSequence() { return stopSequence; }
    public int getStopHeadsign() { return stopHeadsign; }
    public int getPickupType() { return pickupType; }
    public int getDropOffType() { return dropOffType; }
    public double getShapeDistTraveled() { return shapeDistTraveled; }
    public int getTimepoint() { return timepoint; }
}
