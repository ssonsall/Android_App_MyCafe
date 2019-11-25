package dev.ssonsallsub.mycafe.gps;

public class RequestCaffeLocation {

    private String caffeName;
    private double caffeLatitude;
    private double caffeLongitude;
//    private String caffePhone;
//    private String caffeAddress;
//    private String caffeRoadAddress;
    /*상세정보에 전화번호, 주소 2개 다 있음
    * WebView 다이얼로그로 띄우자! 그게 개이득!*/
    private String caffeUrl;


    public RequestCaffeLocation(){}

    public RequestCaffeLocation(double caffeLatitude, double caffeLongitude, String caffeName) {
        this.caffeLatitude = caffeLatitude;
        this.caffeLongitude = caffeLongitude;
        this.caffeName = caffeName;
    }

    public String getCaffeName() {
        return caffeName;
    }

    public void setCaffeName(String caffeName) {
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

    public String getCaffeUrl() {
        return caffeUrl;
    }

    public void setCaffeUrl(String caffeUrl) {
        this.caffeUrl = caffeUrl;
    }
}
