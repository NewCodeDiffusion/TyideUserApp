package com.codediffusion.tyideuser.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.codediffusion.tyideuser.ApiCalingRetrofit.GlobalClassApiCall;
import com.codediffusion.tyideuser.BookingSummaryAndInvoiceActivity;
import com.codediffusion.tyideuser.Extra.CommonData;
import com.codediffusion.tyideuser.Mapview.HomeActivity;
import com.codediffusion.tyideuser.R;
import com.codediffusion.tyideuser.adapter.AdapterWalletHistory;
import com.codediffusion.tyideuser.model.modelWalletHistory;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.codediffusion.tyideuser.Extra.CommonData.hideLoading;
import static com.codediffusion.tyideuser.Extra.CommonData.showLoading;

public class Wallet extends AppCompatActivity implements PaymentResultListener {
    private TextView tv_CurrentBalance, tv_dataNotFound;
    private RecyclerView rv_WalletHistory;
    private ImageView ivBack;
    private ActionBar actionBar;
    private Button btn_AddAmount;
    private String PayAmount, EnterAmount, PaymentID;
    private EditText et_AddAmount;
    private String UserID, UserWalletAmount;
    private SharedPreferences sharedPreferences;
    private CompositeDisposable disposable = new CompositeDisposable();

    ///////////////////WalletHistory///
    private AdapterWalletHistory adapterWalletHistory;
    private List<modelWalletHistory.WalletHistory> walletHistoryList = new ArrayList<>();


    ///rozerPay
    private Checkout chackout;
    private String razorpayKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        actionBar = getSupportActionBar();
        actionBar.hide();


        sharedPreferences = getSharedPreferences(CommonData.UserData, MODE_PRIVATE);

        if (sharedPreferences.contains(CommonData.UserID)) {

            UserID = sharedPreferences.getString(CommonData.UserID, "");
        }

        Initialization();


    }

    private void Initialization() {

        ivBack = findViewById(R.id.btnBack);
        tv_dataNotFound = findViewById(R.id.tv_dataNotFound);
        rv_WalletHistory = findViewById(R.id.rv_WalletHistory);
        tv_CurrentBalance = findViewById(R.id.tv_CurrentBalance);
        btn_AddAmount = findViewById(R.id.btn_AddAmount);
        et_AddAmount = findViewById(R.id.et_AddAmount);


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();

            }
        });


        btn_AddAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Validation();
            }
        });


        LoadUserWalletAmount();


        LoadUserWalletHistory();


    }

    private void LoadUserWalletHistory() {
        showLoading(Wallet.this, "Please Wait");

        walletHistoryList.clear();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", UserID);


        disposable.add(GlobalClassApiCall.initRetrofit().GetWalletHistory(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((user, throwatble) -> {
                            hideLoading();
                            if (user != null) {

                                Log.e("jksdhkfhk", "Response size: " + new Gson().toJson(user) + "");


                                if (user.getStatus().equals("200")) {

                                    Toast.makeText(Wallet.this, user.getMessage() + "", Toast.LENGTH_LONG).show();

                                    for (int i = 0; i < user.getData().getWalletHistoryList().size(); i++) {

                                        walletHistoryList.add(user.getData().getWalletHistoryList().get(i));

                                    }


                                    adapterWalletHistory = new AdapterWalletHistory(walletHistoryList, getApplicationContext());
                                    rv_WalletHistory.setAdapter(adapterWalletHistory);
                                    adapterWalletHistory.notifyDataSetChanged();


                                } else {
                                    // Toast.makeText(activity, user.getMessage() + "", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                //hideLoading();
                                Toast.makeText(Wallet.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }

                        }
                )
        );


    }

    private void Validation() {
        EnterAmount = et_AddAmount.getText().toString();

        if (TextUtils.isEmpty(EnterAmount)) {
            et_AddAmount.setError("Enter Wallet Amount");
            et_AddAmount.requestFocus();
            return;
        }


        if (Integer.parseInt(EnterAmount) < 10) {

            Toast.makeText(this, "Enter Amount Will Be Grater than 10 RS", Toast.LENGTH_SHORT).show();
            return;
        }

        String convertedAmount = String.valueOf(Integer.parseInt(EnterAmount) * 100);
        PayAmount = EnterAmount;
        rezorpayCall("Qasim", "pasha@gmail.com", "8859228213", convertedAmount);


    }


    private void LoadUserWalletAmount() {
        //showLoading(BookingSummaryAndInvoiceActivity.this, "Please Wait");


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", UserID);


        disposable.add(GlobalClassApiCall.initRetrofit().GetWalletAmountData(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((user, throwatble) -> {
                            // hideLoading();
                            if (user != null) {

                                Log.e("jksdhkfhk", "Response size: " + new Gson().toJson(user) + "");


                                if (user.getStatus().equals("200")) {

                                    Toast.makeText(Wallet.this, user.getMessage() + "", Toast.LENGTH_LONG).show();

                                    // binding.tvUseWalletAmount.setText("Wallet Amount"+" "+getString(R.string.Rs) + " " + user.getData().getTotalWalletAmount());

                                    tv_CurrentBalance.setText("Available Tyide Balance" + getString(R.string.Rs) + " " + user.getData().getTotalWalletAmount());


                                    UserWalletAmount = user.getData().getTotalWalletAmount();

                                } else {
                                    // Toast.makeText(activity, user.getMessage() + "", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                //hideLoading();
                                Toast.makeText(Wallet.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }

                        }
                )
        );

    }


    private void LoadWalletRecharge() {
        showLoading(Wallet.this, "Please Wait");


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", UserID);
        hashMap.put("old_amount", UserWalletAmount);
        hashMap.put("amount", PayAmount);
        hashMap.put("transition_id", PaymentID);

        Log.e("kjhgf", hashMap + "");


        disposable.add(GlobalClassApiCall.initRetrofit().GetWalletRecharge(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((user, throwatble) -> {
                            hideLoading();
                            if (user != null) {

                                Log.e("jksdhkfhk", "Response size: " + new Gson().toJson(user) + "");


                                if (user.getStatus().equals("200")) {

                                    Toast.makeText(Wallet.this, user.getMessage() + "", Toast.LENGTH_LONG).show();

                                    // binding.tvUseWalletAmount.setText("Wallet Amount"+" "+getString(R.string.Rs) + " " + user.getData().getTotalWalletAmount());

                                    LoadUserWalletAmount();


                                } else {
                                    // Toast.makeText(activity, user.getMessage() + "", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                //hideLoading();
                                Toast.makeText(Wallet.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }

                        }
                )
        );

    }


    public void rezorpayCall(String name, String email, String phNo, String convertedAmount) {

        //CommonData.showLoading(context, "Please Wait");
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        // razorpayKey = "razorpayKey"; //Generate your razorpay key from Settings-> API Keys-> copy Key Id
        //razorpayKey = "rzp_live_gcVN7HNkZIliDS"; //Generate your razorpay key from Settings-> API Keys-> copy Key Id
        razorpayKey = "rzp_test_AyFjT1PXxj0piN"; //Generate your razorpay key from Settings-> API Keys-> copy Key Id


        //razorpayKey = "rzp_live_z9P0CARvfz3lbX"; //Generate your razorpay key from Settings-> API Keys-> copy Key Id


        chackout = new Checkout();
        chackout.setKeyID(razorpayKey);
        try {

            JSONObject options = new JSONObject();
            options.put("name", name);
            options.put("description", "Razorpay Payment");
            options.put("currency", "INR");
            options.put("amount", convertedAmount);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", phNo);
            options.put("prefill", preFill);

            chackout.open(Wallet.this, options);
        } catch (Exception e) {
            Toast.makeText(Wallet.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        // After successful payment Razorpay send back a unique id
        Toast.makeText(Wallet.this, "Transaction Successful: " + razorpayPaymentID, Toast.LENGTH_LONG).show();


        PaymentID = razorpayPaymentID;

        Log.e("kdljglk", "Transaction Successful:" + razorpayPaymentID);


        LoadWalletRecharge();

        et_AddAmount.setText("");

       // LoadUserWalletHistory();


        //startActivity(new Intent(Fare_Detail_grActivity.this, ));

    }

    @Override
    public void onPaymentError(int i, String error) {
        // Error message
        Toast.makeText(Wallet.this, "Transaction Failed: " + error, Toast.LENGTH_LONG).show();
    }


}