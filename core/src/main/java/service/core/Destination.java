package service.core;

public class Destination {

    private String name;
    private double latitudeNorth;
    private double latitudeSouth;
    private double longitudeWest;
    private double longitudeEast;

    public Destination() {}

    public Destination(String name, double latitudeNorth, double latitudeSouth, double longitudeWest, double longitudeEast){
        this.name = name;
        this.latitudeNorth = latitudeNorth;
        this.latitudeSouth = latitudeSouth;
        this.longitudeEast = longitudeEast;
        this.longitudeWest = longitudeWest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitudeNorth() {
        return latitudeNorth;
    }

    public void setLatitudeNorth(double latitudeNorth) {
        this.latitudeNorth = latitudeNorth;
    }

    public double getLatitudeSouth() {
        return latitudeSouth;
    }

    public void setLatitudeSouth(double latitudeSouth) {
        this.latitudeSouth = latitudeSouth;
    }

    public double getLongitudeWest() {
        return longitudeWest;
    }

    public void setLongitudeWest(double longitudeWest) {
        this.longitudeWest = longitudeWest;
    }

    public double getLongitudeEast() {
        return longitudeEast;
    }

    public void setLongitudeEast(double longitudeEast) {
        this.longitudeEast = longitudeEast;
    }
}
