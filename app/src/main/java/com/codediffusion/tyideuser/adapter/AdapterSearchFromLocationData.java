package com.codediffusion.tyideuser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.codediffusion.tyideuser.R;
import com.codediffusion.tyideuser.databinding.LayoutSearchDataBinding;
import com.codediffusion.tyideuser.model.modelToAndFromCityDataApi;

import java.util.List;

public class AdapterSearchFromLocationData extends RecyclerView.Adapter<AdapterSearchFromLocationData.AdapterHolder> {
    private List<modelToAndFromCityDataApi.CityList>dataList;
    private Context context;

    public SelectFromLocation selectFromLocation;
    public interface SelectFromLocation {
        void  ChangeLocation(String FromLocation);
    }

    public AdapterSearchFromLocationData(List<modelToAndFromCityDataApi.CityList> dataList, Context context, SelectFromLocation selectFromLocation) {
        this.dataList = dataList;
        this.context = context;
        this.selectFromLocation=selectFromLocation;
    }

    @NonNull
    @Override
    public AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_data,parent,false);

       return new AdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSearchFromLocationData.AdapterHolder holder, int position) {

        modelToAndFromCityDataApi.CityList model=dataList.get(position);

        holder.binding.tvSearchTittle.setText(model.getCityName());

        holder.binding.tvSearchTittle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FromLocation=model.getCityName();

                selectFromLocation.ChangeLocation(FromLocation);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class AdapterHolder extends RecyclerView.ViewHolder {
       private LayoutSearchDataBinding binding;
        public AdapterHolder(@NonNull View itemView) {
            super(itemView);

            binding= DataBindingUtil.bind(itemView);

            if (binding!=null){
                binding.executePendingBindings();
            }
        }
    }
}
