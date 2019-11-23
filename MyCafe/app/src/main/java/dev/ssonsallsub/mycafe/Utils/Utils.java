package dev.ssonsallsub.mycafe.Utils;

import java.util.ArrayList;
import java.util.List;

import dev.ssonsallsub.mycafe.coffeebrand.CoffeeBrandName;
import dev.ssonsallsub.mycafe.gps.MyCurrentLocation;
import dev.ssonsallsub.mycafe.gps.RequestCaffeLocation;

public class Utils {
    public static List<RequestCaffeLocation> getCafeLocation(int cafeName) {
        switch (cafeName) {
            case CoffeeBrandName.All_CAFE:
                //내 좌표 가지고 와서
                MyCurrentLocation myCurrentLocation = MyCurrentLocation.getInstance();
                myCurrentLocation.setMyCurrentLatitude(35.1838137);
                myCurrentLocation.setMyCurrentLongitude(129.0021623);

                //REST API로 요청
                ArrayList<RequestCaffeLocation> requestCaffeLocationst =
                        requestRestAPI(myCurrentLocation.getMyCurrentLatitude(),myCurrentLocation.getMyCurrentLongitude());

                //이 어레이리스트를 리턴하는게 아니라 스태틱 같은걸로 들고 있고
                //온클릭마다 리스트 비우고 새데이터 집어넣고 메인액티비티에서 그 데이터로 마커찍고
                //그럼 되려나?
                return requestCaffeLocationst;
            case CoffeeBrandName.ANGEL_IN_US:

                break;
            default:
                break;
        }
        return null;
    }

    public static ArrayList<RequestCaffeLocation> requestRestAPI(double y, double x){
        int rect = 5000; // 5km
        //https 통신으로 카페데이터 요청

        //받으면 gson으로 파싱해서

        //어레이리스트에 집어놓고
        ArrayList<RequestCaffeLocation> requestCaffeLocationst = new ArrayList<RequestCaffeLocation>();

        //리턴
        return null;
    }
}
