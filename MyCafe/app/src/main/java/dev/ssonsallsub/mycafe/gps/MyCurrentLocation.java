package dev.ssonsallsub.mycafe.gps;

public class MyCurrentLocation {
    private double myCurrentLatitude;
    private double myCurrentLongitude;

    private MyCurrentLocation(){}
    private static MyCurrentLocation instance = new MyCurrentLocation();
    public static MyCurrentLocation getInstance(){
        return instance;
    }

    public double getMyCurrentLatitude() {
        return myCurrentLatitude;
    }

    public void setMyCurrentLatitude(double myCurrentLatitude) {
        this.myCurrentLatitude = myCurrentLatitude;
    }

    public double getMyCurrentLongitude() {
        return myCurrentLongitude;
    }

    public void setMyCurrentLongitude(double myCurrentLongitude) {
        this.myCurrentLongitude = myCurrentLongitude;
    }
}
