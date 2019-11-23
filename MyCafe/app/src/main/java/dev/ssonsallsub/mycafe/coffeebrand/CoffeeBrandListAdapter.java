package dev.ssonsallsub.mycafe.coffeebrand;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dev.ssonsallsub.mycafe.R;
import dev.ssonsallsub.mycafe.Utils.Utils;

public class CoffeeBrandListAdapter extends RecyclerView.Adapter<CoffeeBrandListAdapter.CoffeeBrandListViewHolder> {
    private ArrayList<CoffeeBrandData> coffeeBrandDataList;

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
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        Utils.getCafeLocation(position);
                    }
                }
            });
        }
    }
}
