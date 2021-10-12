package com.codediffusion.tyideuser.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codediffusion.tyideuser.ApiCalingRetrofit.GlobalClassApiCall;
import com.codediffusion.tyideuser.Extra.CommonData;
import com.codediffusion.tyideuser.R;
import com.codediffusion.tyideuser.adapter.AdapterAllBooking;
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


public class FragmentAllBooking extends Fragment {
    private RecyclerView rv_AllBooking;
    private AdapterAllBooking adapterAllBooking;
    private List<modelUserBookingData.Datum> complainsList = new ArrayList<>();

    private CompositeDisposable disposable = new CompositeDisposable();
    private SharedPreferences sharedPreferences;
    private String USERID;


    public FragmentAllBooking() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_booking, container, false);


        sharedPreferences = getActivity().getSharedPreferences(CommonData.UserData, Context.MODE_PRIVATE);

        if (sharedPreferences.contains(CommonData.UserID)) {
            USERID = sharedPreferences.getString(CommonData.UserID, "");
        }

        initialization(view);

        return view;
    }

    private void initialization(View view) {
        rv_AllBooking = view.findViewById(R.id.rv_AllBooking);
        LoadAllBookingData();

    }

    private void LoadAllBookingData() {
        complainsList.clear();
        showLoading(getActivity(), "Please Wait");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", USERID);
        hashMap.put("type", "0");

        Log.e("jfdkjhk", hashMap + "");

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
                                        complainsList.add(user.getData().get(i));
                                    }

                                    adapterAllBooking = new AdapterAllBooking(complainsList, getActivity());

                                    rv_AllBooking.setAdapter(adapterAllBooking);
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