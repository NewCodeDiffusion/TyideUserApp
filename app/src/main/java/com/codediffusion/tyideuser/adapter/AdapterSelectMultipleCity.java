package com.codediffusion.tyideuser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.codediffusion.tyideuser.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class AdapterSelectMultipleCity extends RecyclerView.Adapter<AdapterSelectMultipleCity.CityHolder> {
    private ArrayList<String> cityList;
    private Context context;


    public AdapterSelectMultipleCity(ArrayList<String> cityList, Context context) {
        this.cityList = cityList;
        this.context = context;
    }

    @Override
    public CityHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_select_multiple_location,parent,false);

       return new CityHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSelectMultipleCity.CityHolder holder, int position) {

      //  modelSelectMultipleCity model=cityList.get(position);

        holder.tv_selectCityName.setText(cityList.get(position));

        holder.iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.tv_selectCityName.setText("");
                holder.Rl_multipleCity.setVisibility(View.GONE);
                holder.iv_cross.setVisibility(View.GONE);
                holder.View_green.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class CityHolder extends RecyclerView.ViewHolder {

        private TextView tv_selectCityName;
        private ImageView iv_cross;
        private RelativeLayout Rl_multipleCity;
        private View View_green;

        public CityHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tv_selectCityName=itemView.findViewById(R.id.tv_selectCityName);
            iv_cross=itemView.findViewById(R.id.iv_cross);
            Rl_multipleCity=itemView.findViewById(R.id.Rl_multipleCity);
            View_green=itemView.findViewById(R.id.View_green);
        }
    }
}
