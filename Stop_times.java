public class Stop_times {
    private  String tripId;
    private  String arrivalTime;
    private  String departureTime;
    private  String stopId;
    private  int stopSequence;
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

    public String getTripId() { return this.tripId; }
    public String getArrivalTime() { return this.arrivalTime; }
    public String getDepartureTime() { return this.departureTime; }
    public String getStopId() { return this.stopId; }
    public int getStopSequence() { return this.stopSequence; }
    public int getStopHeadsign() { return this.stopHeadsign; }
    public int getPickupType() { return this.pickupType; }
    public int getDropOffType() { return this.dropOffType; }
    public double getShapeDistTraveled() { return this.shapeDistTraveled; }
    public int getTimepoint() { return this.timepoint; }
}
