package dev.ssonsallsub.mycafe;

import android.Manifest;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.content.pm.Signature;

import android.location.LocationManager;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import dev.ssonsallsub.mycafe.coffeebrand.CoffeeBrandData;
import dev.ssonsallsub.mycafe.coffeebrand.CoffeeBrandListAdapter;
import dev.ssonsallsub.mycafe.gps.GpsTracker;
import dev.ssonsallsub.mycafe.gps.MyCurrentLocation;

import static com.kakao.util.maps.helper.Utility.getPackageInfo;


public class MainActivity extends AppCompatActivity
        implements MapView.CurrentLocationEventListener,
        MapReverseGeoCoder.ReverseGeoCodingResultListener {

    private RecyclerView coffeeBrandListView;
    private CoffeeBrandListAdapter coffeeBrandListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private DrawerLayout moreMenuLayout;
    private View moreMenuView;
    private TextView btn_close;
    private ImageView btn_more;

    /*맵 관련*/
    private MapView mMapView;
    private ImageView btn_mylocation;
    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*KaKao Map 사용을 위한 해시값 구하기
        * 맵이 갑자기 안뜨면 Appkey 바꾸고
        * Hash값 다시 뽑아서 등록해보기*/
        //Log.e("hashKey", getKeyHash(this));

        /*기본 맵 띄우기*/
        mMapView = new MapView(this);
        ViewGroup mapViewContainer = findViewById(R.id.map_view);
        mapViewContainer.addView(mMapView);


        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {
            checkRunTimePermission();
        }


        /* 현재 위치로 이동 버튼 구현 */
        /* 위치 추적 모드 on(현재위치+카메라이동) / off (현재위치만) / 위치잡기끄기 전환 되도록 구현필요*/
        btn_mylocation = findViewById(R.id.btn_mylocation);
        btn_mylocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gpsTracker = new GpsTracker(MainActivity.this);
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                Log.d("LocTest","latitude >> " + latitude + ", longitude >> " + longitude);


                MyCurrentLocation myCurrentLocation = MyCurrentLocation.getInstance();
                myCurrentLocation.setMyCurrentLatitude(latitude);
                myCurrentLocation.setMyCurrentLongitude(longitude);

                mMapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude,longitude),true);
            }
        });

        /*옆에서 튀어나오는 메뉴 , 이걸 뭐라 그러지?*/
        moreMenuLayout = findViewById(R.id.more_menu_layout);
        moreMenuView = findViewById(R.id.more_menu_view);

        btn_more = findViewById(R.id.btn_more);
        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreMenuLayout.openDrawer(moreMenuView);
            }
        });

        btn_close = findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreMenuLayout.closeDrawer(moreMenuView);
            }
        });

        /*리사이클러뷰 이용한 커피브랜드 로고 가로리스트뷰*/
        coffeeBrandListView = findViewById(R.id.list_coffee_brand);
        ArrayList<CoffeeBrandData> data = new ArrayList<>();

        data.add(new CoffeeBrandData(R.mipmap.logo_allcafe_round));
        data.add(new CoffeeBrandData(R.mipmap.logo_angelinus_round));
        data.add(new CoffeeBrandData(R.mipmap.logo_caffebene_round));
        data.add(new CoffeeBrandData(R.mipmap.logo_hollys_round));
        data.add(new CoffeeBrandData(R.mipmap.logo_backdabang_round));
        data.add(new CoffeeBrandData(R.mipmap.logo_ediya_round));
        data.add(new CoffeeBrandData(R.mipmap.logo_davinci_round));
        data.add(new CoffeeBrandData(R.mipmap.logo_tomtom_round));
        data.add(new CoffeeBrandData(R.mipmap.logo_pascucci_round));
        data.add(new CoffeeBrandData(R.mipmap.logo_coffeebay_round));
        data.add(new CoffeeBrandData(R.mipmap.logo_compose_round));
        data.add(new CoffeeBrandData(R.mipmap.logo_starbucks_round));
        data.add(new CoffeeBrandData(R.mipmap.logo_twosome_round));


        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); //가로 세팅

        coffeeBrandListView.setLayoutManager(linearLayoutManager);
        coffeeBrandListAdapter = new CoffeeBrandListAdapter();

        coffeeBrandListAdapter.setCoffeeBrandData(data);
        coffeeBrandListView.setAdapter(coffeeBrandListAdapter);

    }

    /*지도 관련해서 GPS 등 메서드 오버라이드*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mMapView.setShowCurrentLocationMarker(false);
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
    }


    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {}

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {}

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {}

    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        mapReverseGeoCoder.toString();
        onFinishReverseGeoCoding(s);
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
        onFinishReverseGeoCoding("Fail");
    }

    private void onFinishReverseGeoCoding(String result) {}


    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {
                //위치 값을 가져올 수 있음
                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                } else {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음
            mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 GPS 기능이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("GPS 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 GPS가 필요합니다.\n"
                + "GPS를 켜시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("GPS 켜기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("ttttt", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }
}
