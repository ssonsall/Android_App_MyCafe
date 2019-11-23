package dev.ssonsallsub.mycafe.gps;

public class RequestCaffeLocation {

    private double caffeLatitude;
    private double caffeLongitude;

    public RequestCaffeLocation(){}

    public RequestCaffeLocation(double caffeLatitude, double caffeLongitude) {
        this.caffeLatitude = caffeLatitude;
        this.caffeLongitude = caffeLongitude;
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
}
