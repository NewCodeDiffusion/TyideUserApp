package com.codediffusion.tyideuser.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codediffusion.tyideuser.ApiCalingRetrofit.GlobalClassApiCall;
import com.codediffusion.tyideuser.Extra.CommonData;
import com.codediffusion.tyideuser.R;
import com.codediffusion.tyideuser.adapter.AdapterAllBooking;
import com.codediffusion.tyideuser.adapter.AdapterCompleteBooking;
import com.codediffusion.tyideuser.databinding.FragmentCompleteBookingBinding;
import com.codediffusion.tyideuser.model.modelUserBookingData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.codediffusion.tyideuser.Extra.CommonData.hideLoading;


public class FragmentCompleteBooking extends Fragment {

    private FragmentCompleteBookingBinding binding;

   // private AdapterCompleteBooking adapterCompleteBooking;
    private List<modelUserBookingData.Datum> completeBookingList = new ArrayList<>();
    private AdapterCompleteBooking adapterAllBooking;

    private CompositeDisposable disposable = new CompositeDisposable();
    private SharedPreferences sharedPreferences;
    private String USERID;



    public FragmentCompleteBooking() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_complete_booking, container, false);

        sharedPreferences = getActivity().getSharedPreferences(CommonData.UserData, Context.MODE_PRIVATE);

        if (sharedPreferences.contains(CommonData.UserID)) {
            USERID = sharedPreferences.getString(CommonData.UserID, "");
        }


        iniitialize();
        return binding.getRoot();
    }

    private void iniitialize() {


        LoadCompleteBookingData();

    }

    private void LoadCompleteBookingData() {
        completeBookingList.clear();

        //showLoading(getActivity(), "Please Wait");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", USERID);
        hashMap.put("type", "4");

        Log.e("paramsjdfkjkgjkg", hashMap + "");

///

        disposable.add(GlobalClassApiCall.initRetrofit().BookingListData(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((user, throwatble) -> {
                            hideLoading();
                            if (user != null) {

                                Log.e("jksdhkfhk", "Response size: " + new Gson().toJson(user) + "");


                                if (user.getStatus().equals("200")) {

                                    for (int i = 0; i < user.getData().size(); i++) {
                                        completeBookingList.add(user.getData().get(i));
                                    }

                                    adapterAllBooking = new AdapterCompleteBooking(completeBookingList, getActivity());

                                    binding.rvOngoingBooking.setAdapter(adapterAllBooking);
                                    adapterAllBooking.notifyDataSetChanged();



                                } else {
                                    Toast.makeText(getActivity(),   "Booking List Not Found", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getActivity(), "Booking List Not Found", Toast.LENGTH_SHORT).show();
                            }

                        }
                )
        );

       /* completeBookingList.add(new modelAllBooking("123456", "19_03_2021", "Ajay", "8596258965", "Okhla New Delhi", "Noida", "Complete", "Pay During Trip", "1250", "5:15am", "20-03-2021", "Swift Dzire CNG"));
        completeBookingList.add(new modelAllBooking("123456", "19_03_2021", "Ajay", "8596258965", "Okhla New Delhi", "Noida", "Complete", "Pay During Trip", "1250", "5:15am", "20-03-2021", "Swift Dzire CNG"));
        completeBookingList.add(new modelAllBooking("123456", "19_03_2021", "Ajay", "8596258965", "Okhla New Delhi", "Noida", "Complete", "Pay During Trip", "1250", "5:15am", "20-03-2021", "Swift Dzire CNG"));

        adapterCompleteBooking = new AdapterCompleteBooking(completeBookingList, getActivity());

        binding.rvOngoingBooking.setAdapter(adapterCompleteBooking);
        adapterCompleteBooking.notifyDataSetChanged();*/


    }
}