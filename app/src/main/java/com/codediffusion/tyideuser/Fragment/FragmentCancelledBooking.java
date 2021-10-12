package com.codediffusion.tyideuser.Fragment;

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
import com.codediffusion.tyideuser.R;
import com.codediffusion.tyideuser.adapter.AdapterAllBooking;
import com.codediffusion.tyideuser.databinding.FragmentCancelledBookingBinding;
import com.codediffusion.tyideuser.model.modelUserBookingData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.codediffusion.tyideuser.Extra.CommonData.hideLoading;
import static com.codediffusion.tyideuser.Extra.CommonData.showLoading;


public class FragmentCancelledBooking extends Fragment {

    private FragmentCancelledBookingBinding binding;

    private List<modelUserBookingData.Datum> completeBookingList = new ArrayList<>();
    private AdapterAllBooking adapterAllBooking;

    private CompositeDisposable disposable = new CompositeDisposable();
    private SharedPreferences sharedPreferences;
    private String USERID;


    public FragmentCancelledBooking() {
        // Required empty public constructor
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_cancelled_booking, container, false);

       initialize();

        return binding.getRoot();
    }

    private void initialize() {

        LoadCancelledBookingData();


    }

    private void LoadCancelledBookingData() {
       // completeBookingList.clear();
        showLoading(getActivity(), "Please Wait");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", USERID);
        hashMap.put("type", "5");

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

                                    adapterAllBooking = new AdapterAllBooking(completeBookingList, getActivity());

                                    binding.rvCancelledBooking.setAdapter(adapterAllBooking);
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
    }
}