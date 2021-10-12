package com.codediffusion.tyideuser.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codediffusion.tyideuser.ApiCalingRetrofit.GlobalClassApiCall;
import com.codediffusion.tyideuser.Extra.CommonData;
import com.codediffusion.tyideuser.Fare_Detail_grActivity;
import com.codediffusion.tyideuser.R;

import com.codediffusion.tyideuser.databinding.LayoutSelectCarBinding;
import com.codediffusion.tyideuser.model.modelCarListData;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.codediffusion.tyideuser.Extra.CommonData.hideLoading;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder>{

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private CompositeDisposable disposable=new CompositeDisposable();
    private Context context;
    private String USERID;
    private int Calculate20PerPrice;
    private List<modelCarListData.City> carsModelList;

    public CarAdapter(Context context, List<modelCarListData.City> carsModelList) {
        this.context = context;
        this.carsModelList = carsModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_select_car,parent,false);

        sharedPreferences=context.getSharedPreferences(CommonData.UserData,Context.MODE_PRIVATE);

        editor=sharedPreferences.edit();
        if (sharedPreferences.contains(CommonData.UserID)){

             USERID=sharedPreferences.getString(CommonData.UserID,"");

        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        modelCarListData.City model=carsModelList.get(position);
        holder.binding.tvCarName.setText(model.getShorDescription());
        holder.binding.tvCarType.setText(model.getCarName());
        holder.binding.tvTotalKML.setText(model.getTotalKm()+""+"Km");
        holder.binding.tvFullPrice.setText(context.getString(R.string.Rs)+" "+model.getFixedPrice());
        holder.binding.tvDriverCharges.setText(model.getDriverAll());

        holder.binding.tvDriverName.setText("Ashok Kumar");
        holder.binding.tvDriverExp.setText("2 Years");

       // holder.binding.tvNightCharges.setText(context.getString(R.string.Rs)+" "+model.getNightCharges());
        holder.binding.tvTimeduration.setText(model.getTotalDays()+""+"Day");



        Glide.with(context).load(model.getCarImage()).into(holder.binding.ivCarImage);


        holder.binding.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Calculate20PerPrice=Integer.parseInt(model.getFixedPrice())*model.getBookingPer()/100;

                Log.e("lgkhkjghjk",Calculate20PerPrice+"");


                CallCreateBookingApi(model);



            }
        });


    }



    @Override
    public int getItemCount() {
        return carsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutSelectCarBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);

            binding= DataBindingUtil.bind(itemView);
            if (binding!=null){
                binding.executePendingBindings();
            }

        }
    }

    private void CallCreateBookingApi(modelCarListData.City model) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", USERID);
        hashMap.put("from_place",model.getFromPlace());
        hashMap.put("to_place", model.getToPlace());
        hashMap.put("return_date",model.getReturnDate());
        hashMap.put("pick_up_date", model.getPickUpDate());
        hashMap.put("pick_up_at_date",model.getPickUpAtDate());
        hashMap.put("car_name", model.getCarName());
        hashMap.put("trip_type", model.getTripType());
        hashMap.put("night_allowance", "0");
        hashMap.put("get_gst_charges", "0");

        hashMap.put("running_distance", model.getTotalKm());
        hashMap.put("number_of_days",model.getTotalDays());
        hashMap.put("extra_charge_kilo", "0");
        hashMap.put("extra_hour_rate", "0");



        Log.e("paramsjgkjgkjh", hashMap + "");

///

        disposable.add(GlobalClassApiCall.initRetrofit().CreateBookingData(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((user, throwatble) -> {
                            hideLoading();
                            if (user != null) {
                                //  rv_searchList.setVisibility(View.VISIBLE);
                                Log.e("jksdhkfhk", "Search: " + new Gson().toJson(user) + "");


                                if (user.getStatus().equals("200")) {


                                    editor.putString(CommonData.TempararyId, String.valueOf(user.getData().getTempBookginId()));
                                     editor.commit();
                                     editor.apply();

                                    Intent intent=new Intent(context, Fare_Detail_grActivity.class);
                                    intent.putExtra(CommonData.TotalAmount,model.getFixedPrice());
                                    intent.putExtra("TwentyPerAmount",Calculate20PerPrice);
                                    intent.putExtra("RideTime",model.getTotalDays());
                                    intent.putExtra("TotalKm",model.getTotalKm());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    Log.e("UDropDate",Calculate20PerPrice+""+model.getFixedPrice());
                                    Log.e("UDropDate",model.getTotalDays()+""+model.getTotalKm());


                                    context.startActivity(intent);



                                }
                            }

                        }
                )
        );

    }


}
