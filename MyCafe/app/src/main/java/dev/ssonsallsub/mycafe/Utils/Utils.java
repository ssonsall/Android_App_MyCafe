package dev.ssonsallsub.mycafe.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import dev.ssonsallsub.mycafe.MainActivity;
import dev.ssonsallsub.mycafe.caffelocationresponsedata.ResponseData;
import dev.ssonsallsub.mycafe.coffeebrand.CoffeeBrandName;
import dev.ssonsallsub.mycafe.gps.MyCurrentLocation;
import dev.ssonsallsub.mycafe.gps.RequestCaffeLocation;

public class Utils {
    private MyCurrentLocation myCurrentLocation = MyCurrentLocation.getInstance();
    private Context context;
    private MapView mMapView;

    public Utils(MapView mMapView) {
        this.mMapView = mMapView;
    }

    public void getCafeLocation(int cafeName) {
        switch (cafeName) {
            case CoffeeBrandName.All_CAFE:
                //REST API로 요청
                try {
                    new ReqCaffeLocation().execute();
                } catch (Exception e) {
                }

                break;
            case CoffeeBrandName.ANGEL_IN_US:

                break;
            default:
                break;
        }
        //return null;
    }

    class ReqCaffeLocation extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMapView.removeAllPOIItems();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            ArrayList<RequestCaffeLocation> requestCaffeLocationsList = requestRestAPI(myCurrentLocation.getMyCurrentLatitude(), myCurrentLocation.getMyCurrentLongitude());
            return requestCaffeLocationsList;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Object object) {
            super.onPostExecute(object);
            ArrayList<RequestCaffeLocation> requestCaffeLocationsList = (ArrayList<RequestCaffeLocation>) object;

            if (requestCaffeLocationsList != null) {
                //Map에 카페 마커 찍기
                for (int i = 0; i < requestCaffeLocationsList.size(); i++) {
                    MapPOIItem marker = new MapPOIItem();
                    marker.setItemName(requestCaffeLocationsList.get(i).getCaffeName());
                    marker.setTag(i);
                    marker.setMapPoint(MapPoint.mapPointWithGeoCoord(requestCaffeLocationsList.get(i).getCaffeLatitude(), requestCaffeLocationsList.get(i).getCaffeLongitude()));
                    marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                    marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

                    mMapView.addPOIItem(marker);
                }
            }
        }
    }

    public ArrayList<RequestCaffeLocation> requestRestAPI(double y, double x) {
        int radius = 1000; // 5km
        int page = 1;



        ArrayList<RequestCaffeLocation> requestCaffeLocationsList = new ArrayList<>();
        boolean isEnd = true;
        DetailURL detailURL = DetailURL.getInstance();
        if (detailURL.getUrlList() != null) {
            detailURL.getUrlList().clear();
        }

        do {
            //https 통신으로 카페데이터 요청
            //거리순 (사주카페 같은거도 나옴)
            String urlString = "https://dapi.kakao.com/v2/local/search/category.json?category_group_code=CE7&x=" + x + "&y=" + y + "&radius=" + radius + "&page=" + page + "&sort=distance";
            //정확도순 (더 이상함. 학원기준 바로 옆에 컴포즈도 안나옴)
            //String urlString = "https://dapi.kakao.com/v2/local/search/category.json?category_group_code=CE7&x=" + x + "&y=" + y + "&radius=" + radius + "&page=" + page;

            String restApiKey = "fe9d120756644a1c19d175968e4f1192";
            try {
                URL url = new URL(urlString);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "KakaoAK " + restApiKey);
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String inputTmp = "";
                StringBuffer inputData = new StringBuffer();
                inputData.setLength(0);
                while ((inputTmp = br.readLine()) != null) {
                    inputData.append(inputTmp);
                }

                //받으면 gson으로 파싱해서
                Gson gson = new Gson();
                ResponseData responseData = gson.fromJson(inputData.toString(), ResponseData.class);

                for (int i = 0; i < responseData.getDocuments().size(); i++) {
                    RequestCaffeLocation requestCaffeLocation = new RequestCaffeLocation();
                    requestCaffeLocation.setCaffeLatitude(Double.parseDouble(responseData.getDocuments().get(i).getY()));
                    requestCaffeLocation.setCaffeLongitude(Double.parseDouble(responseData.getDocuments().get(i).getX()));
                    requestCaffeLocation.setCaffeName(responseData.getDocuments().get(i).getPlaceName());
                    requestCaffeLocation.setCaffeUrl(responseData.getDocuments().get(i).getPlaceUrl());
                    detailURL.getUrlList().add(responseData.getDocuments().get(i).getPlaceUrl());
                    requestCaffeLocationsList.add(requestCaffeLocation);
                }

                //위에건 All Cafe일때이고
                //이 함수에 파라미터로 포지션 값 넘어온거 받아서
                //if로 for문 각각 작성 밑에 거에 contains 걸어서 걸러내자.
                //responseData.getDocuments().get(i).getCategoryName()

                /*15개 이상 결과가 있을 시 최대 45개까지 데이터를 뽑아온다*/
                /*레퍼런스에는 15*45개까지지만 어떤 이유에서인지 45개까지가 맥스다*/
                /*레퍼런스가 잘못되었든가 API를 잘못 만들었든가 둘 중 하나 어쨋든 45개 맥스*/
                if (responseData.getMeta().getIsEnd()) {
                    isEnd = false;
                } else {
                    page++;
                }

                //어레이리스트에 집어놓고


            } catch (Exception e) {
                return null;
            } finally {
                Log.d("isEnd", "isEnd == " + isEnd);
            }

        } while (isEnd);
        return requestCaffeLocationsList;
    }
}
