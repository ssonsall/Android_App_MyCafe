package dev.ssonsallsub.mycafe.gps;

public class RequestCaffeLocation {

    private String caffeName;
    private double caffeLatitude;
    private double caffeLongitude;

    public RequestCaffeLocation(){}

    public RequestCaffeLocation(double caffeLatitude, double caffeLongitude, String caffeName) {
        this.caffeLatitude = caffeLatitude;
        this.caffeLongitude = caffeLongitude;
        this.caffeName = caffeName;
    }

    public double getCaffeLatitude() {
        return caffeLatitude;
    }

    public void setCaffeLatitude(double caffeLatitude) {
        this.caffeLatitude = caffeLatitude;
    }

    public double getCaffeLongitude() {
        return caffeLongitude;
    }

    public void setCaffeLongitude(double caffeLongitude) {
        this.caffeLongitude = caffeLongitude;
    }

    public String getCaffeName() {
        return caffeName;
    }

    public void setCaffeName(String caffeName) {
        this.caffeName = caffeName;
    }
}
