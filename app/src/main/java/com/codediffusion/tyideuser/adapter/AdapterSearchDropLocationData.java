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

public class AdapterSearchDropLocationData extends RecyclerView.Adapter<AdapterSearchDropLocationData.AdapterHolder> {
    private List<modelToAndFromCityDataApi.CityList>dataList;
    private Context context;

    public SelectDropLocation selectDropLocation;
    public interface SelectDropLocation {
        void  ChangeDropLocation(String FromLocation);
    }

    public AdapterSearchDropLocationData(List<modelToAndFromCityDataApi.CityList> dataList, Context context, SelectDropLocation selectDropLocation) {
        this.dataList = dataList;
        this.context = context;
        this.selectDropLocation=selectDropLocation;
    }

    @NonNull
    @Override
    public AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_data,parent,false);

       return new AdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSearchDropLocationData.AdapterHolder holder, int position) {

        modelToAndFromCityDataApi.CityList model=dataList.get(position);

        holder.binding.tvSearchTittle.setText(model.getCityName());

        holder.binding.tvSearchTittle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dropLocation=model.getCityName();

                selectDropLocation.ChangeDropLocation(dropLocation);
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
