package com.codediffusion.tyideuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codediffusion.tyideuser.ApiCalingRetrofit.GlobalClassApiCall;
import com.codediffusion.tyideuser.Extra.CommonData;
import com.codediffusion.tyideuser.Mapview.HomeActivity;
import com.codediffusion.tyideuser.adapter.AdapterSelectPackage;
import com.codediffusion.tyideuser.databinding.ActivityBookingSummaryAndInvoiceBinding;
import com.google.android.gms.common.internal.service.Common;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.codediffusion.tyideuser.Extra.CommonData.hideLoading;
import static com.codediffusion.tyideuser.Extra.CommonData.showLoading;

public class BookingSummaryAndInvoiceActivity extends AppCompatActivity implements PaymentResultListener {
    private ActivityBookingSummaryAndInvoiceBinding binding;
    private String UserID, TripType, TripFor, FromCityLocation, ToCityLocation, PickUpDate, ReturnDate, PickupTime, TotalKm, CarType, ModelType, PackageIdUserId, PayFullAmount="0", PAYAMOUNT="0", UName, UEmail = "pasha@gmail.com", UMobile, TempBookingId, UserWalletAmount;

    ///rozerPay
    private Checkout chackout;
    private String razorpayKey;
    private int Calculate20PerPrice;

    private SharedPreferences sharedPreferences;


    private CompositeDisposable disposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking_summary_and_invoice);

        sharedPreferences = getSharedPreferences(CommonData.UserData, MODE_PRIVATE);

        UserID = sharedPreferences.getString(CommonData.UserID, "");


        Intent intent = getIntent();
        if (intent.hasExtra(CommonData.PickUpLocation)) {
            TripType = intent.getStringExtra(CommonData.TripType);
            TripFor = intent.getStringExtra(CommonData.TripFor);
            FromCityLocation = intent.getStringExtra(CommonData.PickUpLocation);
            ToCityLocation = intent.getStringExtra(CommonData.DropLocation);
            PickUpDate = intent.getStringExtra(CommonData.PickupDate);
            ReturnDate = intent.getStringExtra(CommonData.DropDate);
            PickupTime = intent.getStringExtra(CommonData.PickUpTime);
            TotalKm = intent.getStringExtra(CommonData.TotalDistance);
            CarType = intent.getStringExtra(CommonData.CarType);
            ModelType = intent.getStringExtra(CommonData.CarModel);
            PackageIdUserId = intent.getStringExtra(CommonData.SelectPackageId);

        }

        Initialization();

        LoadUserWalletAmount();


    }


    private void Initialization() {

        LoadCheckOutApiData();


        binding.llPay20PerAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String convertedAmount = String.valueOf(Calculate20PerPrice * 100);
                PAYAMOUNT = String.valueOf(Calculate20PerPrice);
                rezorpayCall(UName, UEmail, UMobile, convertedAmount);

                // startActivity(new Intent(Fare_Detail_grActivity.this, Payment_Methods_grActivity.class));
            }
        });


        binding.llPayFullAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String convertedAmount = String.valueOf(Integer.parseInt(PayFullAmount) * 100);

                PAYAMOUNT = PayFullAmount;

                Log.e("lkjhgf", PayFullAmount + "");

                rezorpayCall(UName, UEmail, UMobile, convertedAmount);

            }
        });
        binding.llPayFullAmountAtRideTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BookingConfirmed.class);
                intent.putExtra(CommonData.TotalAmount, PayFullAmount);
                intent.putExtra(CommonData.BookingId, TempBookingId);
                intent.putExtra(CommonData.UserName, UName);
                intent.putExtra(CommonData.UserEmail, UEmail);
                intent.putExtra(CommonData.UserMobile, UMobile);
                intent.putExtra(CommonData.PayType, "0");////////1-Online,0-COD
                intent.putExtra(CommonData.PayAmount, "0");
                intent.putExtra(CommonData.WalletUse, "0");
                startActivity(intent);
                finish();

            }
        });

        binding.llPayWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PAYAMOUNT=PayFullAmount;

                Log.e("lkjhgf",PAYAMOUNT+"");
              //  Toast.makeText(BookingSummaryAndInvoiceActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();

                if (Integer.parseInt(UserWalletAmount) < Integer.parseInt(PayFullAmount)) {
                    Toast.makeText(BookingSummaryAndInvoiceActivity.this, "Your Wallet Amount is Less A Full Amount,Recharge A Wallet", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), BookingConfirmed.class);
                    intent.putExtra(CommonData.TotalAmount, PayFullAmount);
                    intent.putExtra(CommonData.BookingId, TempBookingId);
                    intent.putExtra(CommonData.UserName, UName);
                    intent.putExtra(CommonData.UserEmail, UEmail);
                    intent.putExtra(CommonData.UserMobile, UMobile);
                    intent.putExtra(CommonData.PayType, "1");////////1-Online,0-COD,2-Wallet
                    intent.putExtra(CommonData.PayAmount, PAYAMOUNT);
                    intent.putExtra(CommonData.WalletUse, "1");
                    startActivity(intent);
                    finish();
                }


            }
        });


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

                                    Toast.makeText(BookingSummaryAndInvoiceActivity.this, user.getMessage() + "", Toast.LENGTH_LONG).show();

                                    binding.tvUseWalletAmount.setText("Wallet Amount"+" "+getString(R.string.Rs) + " " + user.getData().getTotalWalletAmount());

                                    UserWalletAmount = user.getData().getTotalWalletAmount();

                                } else {
                                    // Toast.makeText(activity, user.getMessage() + "", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                //hideLoading();
                                Toast.makeText(BookingSummaryAndInvoiceActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }

                        }
                )
        );

    }


    private void LoadCheckOutApiData() {


        showLoading(BookingSummaryAndInvoiceActivity.this, "Please Wait");


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("trip_type", TripType);
        hashMap.put("trip_for", TripFor);
        hashMap.put("from_city", FromCityLocation);
        hashMap.put("to_city", ToCityLocation);
        hashMap.put("pick_up_date", PickUpDate);
        hashMap.put("return_date", ReturnDate);
        hashMap.put("pick_up_at_time", PickupTime);
        hashMap.put("kilometer", TotalKm);
        hashMap.put("car_type", CarType);
        hashMap.put("model_type", ModelType);
        hashMap.put("package_id", PackageIdUserId);
        hashMap.put("user_id", UserID);

        Log.e("lkjhgfghjkl",hashMap+"");


        disposable.add(GlobalClassApiCall.initRetrofit().CheckOutData(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((user, throwatble) -> {
                            hideLoading();
                            if (user != null) {

                                Log.e("jksdhkfhk", "Response size: " + new Gson().toJson(user) + "");


                                if (user.getStatus().equals("200")) {

                                    Toast.makeText(BookingSummaryAndInvoiceActivity.this, user.getMessage() + "", Toast.LENGTH_LONG).show();

                                    binding.tvCustomerNAme.setText(user.getData().getCustomerName());
                                    binding.tvCustomerMobile.setText(user.getData().getMobile());
                                    binding.tvTotalAmount.setText(getString(R.string.Rs) + " " + user.getData().getAmount());
                                    binding.tvPickAddress.setText(user.getData().getPickAddress());
                                    binding.tvDropAddress.setText(user.getData().getDropAddress());
                                    binding.tvTripStartTime.setText(user.getData().getTripStart());
                                    binding.tvCarType.setText(user.getData().getCarType());
                                    binding.tvCarModel.setText(user.getData().getCarModel());
                                    binding.tvPickupDate.setText(user.getData().getPickDate());
                                    binding.tvReturnDate.setText(user.getData().getReturnDate());
                                    binding.tvTotalDistance.setText(user.getData().getDistance());
                                    binding.tvSelectPAckage.setText(user.getData().getPackageName());
                                    binding.tvNote.setText(user.getData().getNote());


                                    binding.tvAmmount.setText(getString(R.string.Rs) + " " + user.getData().getAmount());
                                    binding.tvExtraCharge.setText(getString(R.string.Rs) + " " + user.getData().getExtraCharge());
                                    binding.tvGstCharge.setText(getString(R.string.Rs) + " " + user.getData().getGst());
                                    binding.tvFinalAmount.setText(getString(R.string.Rs) + " " + user.getData().getTotalAmount());

                                    PayFullAmount = user.getData().getTotalAmount();


                                    Log.e("lkjhgf", PayFullAmount + "");

                                    UName = user.getData().getCustomerName();
                                    UMobile = user.getData().getMobile();


                                    Calculate20PerPrice = Integer.parseInt(user.getData().getTotalAmount()) * 20 / 100;


                                    binding.tv20perAmount.setText(getString(R.string.Rs) + " " + String.valueOf(Calculate20PerPrice));
                                    binding.tvFullAmount.setText(getString(R.string.Rs) + " " + user.getData().getTotalAmount());
                                    // binding.tvFullAmount.setText(user.getData().getTotalAmount());


                                    TempBookingId = user.getData().getBookingId();


                                } else {
                                    // Toast.makeText(activity, user.getMessage() + "", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(BookingSummaryAndInvoiceActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
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

            chackout.open(BookingSummaryAndInvoiceActivity.this, options);
        } catch (Exception e) {
            Toast.makeText(BookingSummaryAndInvoiceActivity.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        // After successful payment Razorpay send back a unique id
        Toast.makeText(BookingSummaryAndInvoiceActivity.this, "Transaction Successful: " + razorpayPaymentID, Toast.LENGTH_LONG).show();

        Log.e("kdljglk", "Transaction Successful:" + razorpayPaymentID);

        Intent intent = new Intent(getApplicationContext(), BookingConfirmed.class);
        intent.putExtra(CommonData.RozerPayPaymentId, razorpayPaymentID);
        intent.putExtra(CommonData.BookingId, TempBookingId);
        intent.putExtra(CommonData.TotalAmount, PayFullAmount);
        intent.putExtra(CommonData.UserName, UName);
        intent.putExtra(CommonData.UserEmail, UEmail);
        intent.putExtra(CommonData.UserMobile, UMobile);
        intent.putExtra(CommonData.PayType, "1");////////1-Online,0-COD
        intent.putExtra(CommonData.PayAmount, PAYAMOUNT);
        intent.putExtra(CommonData.WalletUse, "0");

        startActivity(intent);
        finish();
        //startActivity(new Intent(Fare_Detail_grActivity.this, ));

    }

    @Override
    public void onPaymentError(int i, String error) {
        // Error message
        Toast.makeText(BookingSummaryAndInvoiceActivity.this, "Transaction Failed: " + error, Toast.LENGTH_LONG).show();
    }


}