public class Route {
    private String id;
    private String shortName;
    private String longName;
    private String color;

    public Route(String id, String shortName, String longName, String color) {
        this.id = id;
        this.shortName = shortName;
        this.longName = longName;
        this.color = color;
    }

    public String getId() { return id; }
    public String getShortName() { return shortName; }
    public String getLongName() { return longName; }
    public String getColor() { return color; }

    @Override
    public String toString() {
        return "Route{" +
               "id='" + id + '\'' +
               ", shortName='" + shortName + '\'' +
               ", longName='" + longName + '\'' +
               ", color='" + color + '\'' +
               '}';
    }
}
