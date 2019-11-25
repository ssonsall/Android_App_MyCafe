package dev.ssonsallsub.mycafe.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;

import com.google.gson.Gson;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import dev.ssonsallsub.mycafe.CaffeLocationResponseData.ResponseData;
import dev.ssonsallsub.mycafe.MainActivity;
import dev.ssonsallsub.mycafe.R;
import dev.ssonsallsub.mycafe.coffeebrand.CoffeeBrandName;
import dev.ssonsallsub.mycafe.gps.GpsTracker;
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


                //이 어레이리스트를 리턴하는게 아니라 스태틱 같은걸로 들고 있고
                //온클릭마다 리스트 비우고 새데이터 집어넣고 메인액티비티에서 그 데이터로 마커찍고
                //그럼 되려나?
                //return requestCaffeLocationsList;
                break;
            case CoffeeBrandName.ANGEL_IN_US:

                break;
            default:
                break;
        }
        //return null;
    }

    public ArrayList<RequestCaffeLocation> requestRestAPI(double y, double x) {
        int radius = 1000; // 5km
        int page = 1;
        ArrayList<RequestCaffeLocation> requestCaffeLocationsList = new ArrayList<>();
        boolean isEnd = true;
        do {
            //https 통신으로 카페데이터 요청
            String urlString = "https://dapi.kakao.com/v2/local/search/category.json?category_group_code=CE7&x=" + x + "&y=" + y + "&radius=" + radius + "&page=" + page + "&sort=distance";
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
                Log.d("jsond", "data >> " + inputData.toString());
                //받으면 gson으로 파싱해서
                Gson gson = new Gson();
                ResponseData responseData = gson.fromJson(inputData.toString(), ResponseData.class);

                for (int i = 0; i < responseData.getDocuments().size(); i++) {
                    RequestCaffeLocation requestCaffeLocation = new RequestCaffeLocation();
                    requestCaffeLocation.setCaffeLatitude(Double.parseDouble(responseData.getDocuments().get(i).getY()));
                    requestCaffeLocation.setCaffeLongitude(Double.parseDouble(responseData.getDocuments().get(i).getX()));
                    requestCaffeLocation.setCaffeName(responseData.getDocuments().get(i).getPlaceName());
                    requestCaffeLocationsList.add(requestCaffeLocation);

                    Log.d("test", "caffe >> " + responseData.getMeta().getIsEnd().toString());
                }

                if (responseData.getMeta().getIsEnd()){
                    isEnd = false;
                }else {
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

    class ReqCaffeLocation extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //ArrayList 비우기

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
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ArrayList<RequestCaffeLocation> requestCaffeLocationsList = (ArrayList<RequestCaffeLocation>) o;

            //Map에 카페 마커 찍기
            for (int i = 0; i < requestCaffeLocationsList.size(); i++) {
                Log.d("async",  requestCaffeLocationsList.size()+"");
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
//requestRestAPI(myCurrentLocation.getMyCurrentLatitude(),myCurrentLocation.getMyCurrentLongitude());