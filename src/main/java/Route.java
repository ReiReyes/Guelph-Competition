
public class Route {
    private String routeId;
    private String agencyId;
    private String shortName;
    private String longName;
    private String description;
    private int routeType;
    private String routeUrl;
    private String routeColor;
    private String textColor;

    public Route(String routeId, String agencyId, String shortName, String longName,
                 String description, int routeType, String routeUrl, String routeColor, String textColor) {
        this.routeId = routeId;
        this.agencyId = agencyId;
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;
        this.routeType = routeType;
        this.routeUrl = routeUrl;
        this.routeColor = routeColor;
        this.textColor = textColor;
    }

    public String getRouteId() { return routeId; }
    public String getAgencyId() { return agencyId; }
    public String getShortName() { return shortName; }
    public String getLongName() { return longName; }
    public String getDescription() { return description; }
    public int getRouteType() { return routeType; }
    public String getRouteUrl() { return routeUrl; }
    public String getRouteColor() { return routeColor; }
    public String getTextColor() { return textColor; }
}
