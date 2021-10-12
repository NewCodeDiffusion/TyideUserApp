package com.codediffusion.tyideuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codediffusion.tyideuser.ApiCalingRetrofit.GlobalClassApiCall;
import com.codediffusion.tyideuser.Extra.CommonData;
import com.codediffusion.tyideuser.adapter.CarAdapter;
import com.codediffusion.tyideuser.databinding.ActivitySelectcarBinding;
import com.codediffusion.tyideuser.model.modelCarListData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.codediffusion.tyideuser.Extra.CommonData.hideLoading;

public class SelectCar extends AppCompatActivity {

    private ActivitySelectcarBinding binding;

   // private RecyclerView rv_SelectCar;
    private CarAdapter carAdapter;
    private List<modelCarListData.City> carsmodelarraylist=new ArrayList<>();
private CompositeDisposable disposable=new CompositeDisposable();
    private SelectCar activity;
    private Context context;


    private  String UPickLocation,UDropLocation,UPickUpDate,UDropDate,UPickUpTime,UTripType,UNewDistance;

    private ArrayList<String>SelectCityData=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.layout_select_car);


        context=activity=this;
        binding= DataBindingUtil.setContentView(this,R.layout.activity_selectcar);



        Intent intent=getIntent();

        if(intent.hasExtra(CommonData.PickUpLocation)){
            UPickLocation=intent.getStringExtra(CommonData.PickUpLocation);
            UDropLocation=intent.getStringExtra(CommonData.DropLocation);
            UPickUpDate=intent.getStringExtra(CommonData.PickupDate);
            UTripType=intent.getStringExtra(CommonData.TripType);

            if (!intent.getStringExtra(CommonData.DropDate).isEmpty()){
                UDropDate=intent.getStringExtra(CommonData.DropDate);

            }else{
                UDropDate="";

            }

            Log.e("bhghgjhj",UTripType+"");
            if (UTripType.equalsIgnoreCase("2")){
                if (!intent.getStringArrayListExtra(CommonData.SelectMultipleCity).isEmpty()){
                    SelectCityData=intent.getStringArrayListExtra(CommonData.SelectMultipleCity);

                    Log.e("fkjkhjk",SelectCityData+"");

                }
            }


            UPickUpTime=intent.getStringExtra(CommonData.PickUpTime);
            UNewDistance=intent.getStringExtra(CommonData.TotalDistance);

            Log.e("fglklhm",UNewDistance+"");


        }


        Initialize();


    }

    private void Initialize() {
        StartSearchCarApi();

       // LoadCarListData();


        binding.ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }



    private void StartSearchCarApi() {
        carsmodelarraylist.clear();
        CommonData.showLoading(context, "Please Wait");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("from_city", UPickLocation);
        hashMap.put("to_city", UDropLocation);
        hashMap.put("trip_type", UTripType);
        hashMap.put("pick_up_date", UPickUpDate);
        hashMap.put("return_date", UDropDate);
        hashMap.put("pick_up_at_time", UPickUpTime);
        hashMap.put("kilometer", UNewDistance);
        if (UTripType.equalsIgnoreCase("2")){
            hashMap.put("malti_location", SelectCityData);
        }else{
            hashMap.put("malti_location", "");
        }



        Log.e("paramsjgkjgkjh", hashMap + "");

///

        disposable.add(GlobalClassApiCall.initRetrofit().GetCarListData(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((user, throwatble) -> {
                            hideLoading();
                            if (user != null) {
                                //  rv_searchList.setVisibility(View.VISIBLE);
                                Log.e("krgjljg", "SearchCar: " + new Gson().toJson(user) + "");


                                if (user.getStatus().equals("200")) {

                                    Toast.makeText(activity, user.getMessage(), Toast.LENGTH_SHORT).show();
                                    for (int i = 0; i < user.getData().getCityList().size(); i++) {
                                        carsmodelarraylist.add(user.getData().getCityList().get(i));
                                    }


                                    carAdapter =new CarAdapter(getApplicationContext(), carsmodelarraylist);
                                    binding.rvSelectCar.setAdapter(carAdapter);
                                    carAdapter.notifyDataSetChanged();



                                }else{
                                    Toast.makeText(activity, "Car not Found", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                )
        );

    }



}