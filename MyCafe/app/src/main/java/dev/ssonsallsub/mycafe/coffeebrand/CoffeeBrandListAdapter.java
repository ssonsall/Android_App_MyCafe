package dev.ssonsallsub.mycafe.coffeebrand;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

import dev.ssonsallsub.mycafe.R;
import dev.ssonsallsub.mycafe.utils.Utils;
import dev.ssonsallsub.mycafe.gps.GpsTracker;
import dev.ssonsallsub.mycafe.gps.MyCurrentLocation;

public class CoffeeBrandListAdapter extends RecyclerView.Adapter<CoffeeBrandListAdapter.CoffeeBrandListViewHolder> {
    private ArrayList<CoffeeBrandData> coffeeBrandDataList;
    Context context;
    MapView mMapView;
    public CoffeeBrandListAdapter(Context context, MapView mMapView) {
        this.context = context;
        this.mMapView = mMapView;
    }

    public void setCoffeeBrandData(ArrayList<CoffeeBrandData> list){
        coffeeBrandDataList = list;
    }

    @NonNull
    @Override
    public CoffeeBrandListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coffee_brand_items,parent,false);
        CoffeeBrandListViewHolder holder = new CoffeeBrandListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeBrandListViewHolder holder, int position) {
        CoffeeBrandData data = coffeeBrandDataList.get(position);
        holder.logo.setImageResource(data.getCoffeeBrandLogo());
    }

    @Override
    public int getItemCount() {
        return coffeeBrandDataList.size();
    }


    class CoffeeBrandListViewHolder extends RecyclerView.ViewHolder{

        private ImageView logo;

        public CoffeeBrandListViewHolder(View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.coffee_brand_logo);
            logo.setBackground(new ShapeDrawable(new OvalShape()));
            logo.setClipToOutline(true);
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*세팅된 버튼 순서대로 현재 상태라면 -> 0: all , 1: 스벅, 2: 엔젤 ...*/
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){

                        /*누른 시점에 내 위치 세팅*/
                        GpsTracker gpsTracker = new GpsTracker(context);
                        double latitude = gpsTracker.getLatitude();
                        double longitude = gpsTracker.getLongitude();
                        //학원좌표로 테스트
//                        latitude = 35.155998;
//                        longitude = 129.059499;
                        MyCurrentLocation myCurrentLocation = MyCurrentLocation.getInstance();
                        myCurrentLocation.setMyCurrentLatitude(latitude);
                        myCurrentLocation.setMyCurrentLongitude(longitude);

                        Utils utils = new Utils(mMapView);
                        utils.getCafeLocation(position);
                    }
                }
            });
        }
    }
}
