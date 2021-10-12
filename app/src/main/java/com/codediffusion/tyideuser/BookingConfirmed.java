package com.codediffusion.tyideuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.airbnb.lottie.BuildConfig;

import com.codediffusion.tyideuser.ApiCalingRetrofit.GlobalClassApiCall;
import com.codediffusion.tyideuser.Extra.CommonData;
import com.codediffusion.tyideuser.Mapview.HomeActivity;
import com.codediffusion.tyideuser.databinding.ActivityBookingConfirmedBinding;
import com.google.gson.Gson;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.codediffusion.tyideuser.Extra.CommonData.hideLoading;

public class BookingConfirmed extends AppCompatActivity {
    private ActivityBookingConfirmedBinding binding;
    private CompositeDisposable disposable = new CompositeDisposable();
    private  String UserId,UTempId,UName,UMobile,UEmail,UPAyAmount,UFullAmount,UTempBookingId,URozerPaymentId,
            UPayType,UWalletUse;

    private SharedPreferences sharedPreferences;
    private String RozerPayPaymentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking_confirmed);

        sharedPreferences=getSharedPreferences(CommonData.UserData,MODE_PRIVATE);

        if (sharedPreferences.contains(CommonData.UserID)){

            UserId=sharedPreferences.getString(CommonData.UserID,"");


        }



        Intent intent = getIntent();

        if (intent.hasExtra(CommonData.BookingId)) {
            UTempBookingId=intent.getStringExtra(CommonData.BookingId);
            URozerPaymentId=intent.getStringExtra(CommonData.RozerPayPaymentId);
            UFullAmount=intent.getStringExtra(CommonData.TotalAmount);
            UName=intent.getStringExtra(CommonData.UserName);
            UMobile=intent.getStringExtra(CommonData.UserMobile);
            UEmail=intent.getStringExtra(CommonData.UserEmail);
            UPAyAmount=intent.getStringExtra(CommonData.PayAmount);
            UPayType=intent.getStringExtra(CommonData.PayType);
            UWalletUse=intent.getStringExtra(CommonData.WalletUse);



            binding.tvPaidAmount.setText(getString(R.string.Rs) + " " +UPAyAmount);


        }

        initialize();


    }

    private void initialize() {


        binding.tvBackToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finishAffinity();

            }
        });

        binding.tvShareBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    i.putExtra(Intent.EXTRA_TEXT, "Hey Checkout this app, " + getString(R.string.app_name) + "\nhttps://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());

                }

            }
        });


        LoadDataCompleteBooking();


    }

    private void LoadDataCompleteBooking() {

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("temp_bookgin_id", UTempBookingId);
        hashMap.put("user_id", UserId);
        hashMap.put("name", UName);
        hashMap.put("mobile", UMobile);
        hashMap.put("email", UEmail);
        hashMap.put("payment_type", UPayType);
        hashMap.put("total_pay", UFullAmount);
        hashMap.put("payment_amount", UPAyAmount);
        hashMap.put("payment_present", "");
        hashMap.put("wallet_use", UWalletUse);

        Log.e("paramsjgkjgkjh", hashMap + "");

///

        disposable.add(GlobalClassApiCall.initRetrofit().GetCompleteOrderData(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((user, throwatble) -> {
                            hideLoading();
                            if (user != null) {
                                //  rv_searchList.setVisibility(View.VISIBLE);
                                Log.e("klojkoiujiouyu", "Search: " + new Gson().toJson(user) + "");


                                if (user.getStatus().equals("200")) {


                                    binding.tvStartDate.setText(user.getData().getFromPlace());

                                    Log.e("lkjhgfdfghjkl",user.getData().getFromPlace());
                                    binding.tvReturenDate.setText(user.getData().getToPlace());
                                    binding.tvBookingId.setText(user.getData().getOrderId());
                                    binding.tvFromDate.setText(user.getData().getPickUpDate());
                                    binding.tvToDate.setText(user.getData().getReturnDate());
                                   // binding.tvPaidAmount.setText(user.getData().getPaymentAmount());





                                    //     Toast.makeText(activity, user.getMessage() + "", Toast.LENGTH_LONG).show();

                                }
                            }

                        }
                )
        );

    }


}