package com.codediffusion.tyideuser;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.codediffusion.tyideuser.Extra.CommonData;
import com.codediffusion.tyideuser.databinding.ActivityFareDetailGrBinding;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class Fare_Detail_grActivity extends AppCompatActivity implements PaymentResultListener {
    private ActivityFareDetailGrBinding binding;

    private Fare_Detail_grActivity activity;
    private Context context;
    private String UName,UEmail,UMobile,TotalAmount,TwentyPerAmount;
    private SharedPreferences sharedPreferences;

    private String TotalKm,TotalTime,PayAmount;

    ///rozerPay
    private Checkout chackout;
    private String razorpayKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_fare_detail_gr);

       sharedPreferences=getSharedPreferences(CommonData.UserData,MODE_PRIVATE);

        context = activity = this;
        if (sharedPreferences.contains(CommonData.UserID)){
            UName=sharedPreferences.getString(CommonData.UserName,"");
            UEmail=sharedPreferences.getString(CommonData.UserEmail,"");
            UMobile=sharedPreferences.getString(CommonData.UserMobile,"");
        }



        Intent intent=getIntent();
        if (intent.hasExtra(CommonData.TotalAmount)){
             // TotalAmount= String.valueOf(intent.getIntExtra("TotalAmount",0));
              TotalAmount= intent.getStringExtra(CommonData.TotalAmount);
              TwentyPerAmount= String.valueOf(intent.getIntExtra("TwentyPerAmount",0));

          //  TotalKm=Integer.parseInt(intent.getStringExtra(CommonData.TotalDistance));
           // TotalTime=Integer.parseInt(intent.getStringExtra(CommonData.RideTime));



            TotalKm=String.valueOf(intent.getIntExtra("TotalKm",0));
            TotalTime=String.valueOf(intent.getIntExtra("RideTime",0));

            Log.e("kfdlgj",TotalAmount+""+TwentyPerAmount);
            Log.e("djkfjg",TotalKm+""+TotalTime);


            binding.tvTotalAmount.setText(getString(R.string.Rs) + " " + TotalAmount);

            binding.tv20perAmount.setText(getString(R.string.Rs) + " " + TwentyPerAmount);
            binding.tvFullAmount.setText(getString(R.string.Rs) + " " + TotalAmount);
            binding.tvFullAmountLater.setText(getString(R.string.Rs) + " " + TotalAmount);
            binding.tvTotalKm.setText(TotalKm+" "+"Km");
            binding.tvTotalTime.setText(TotalTime+" "+"Day");


        }

        Initialization();


    }

    private void Initialization() {




        binding.llPay20PerAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String convertedAmount=String.valueOf(Integer.parseInt(TwentyPerAmount)*100);
                    PayAmount=TwentyPerAmount;
                rezorpayCall(UName, UEmail, UMobile, convertedAmount);

               // startActivity(new Intent(Fare_Detail_grActivity.this, Payment_Methods_grActivity.class));
            }
        });


        binding.llPayFullAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String convertedAmount=String.valueOf(Integer.parseInt(TotalAmount)*100);

                PayAmount=TotalAmount;
               rezorpayCall(UName, UEmail, UMobile, convertedAmount);

            }
        });
        binding.llPayFullAmountAtRideTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),BookingConfirmed.class);
                // intent.putExtra(CommonData.RozerPayPaymentId,razorpayPaymentID);
                intent.putExtra(CommonData.TotalAmount,TotalAmount);
                intent.putExtra(CommonData.Type,"FullAmount");
                intent.putExtra(CommonData.PayAmount,"0");

                startActivity(intent);

            }
        });

        binding.LLApplyPromoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenDialog();
            }
        });


    }

    private void OpenDialog() {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setCancelable(false);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_applycoupan);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.transperent);
        window.setGravity(Gravity.CENTER);


        EditText text = (EditText) dialog.findViewById(R.id.et_applyPromocode);


        Button ApplyButton = (Button) dialog.findViewById(R.id.btn_apply);
        ApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Code Apply SuccessFully", Toast.LENGTH_LONG).show();

                dialog.dismiss();


            }
        });

        dialog.show();

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

            chackout.open(Fare_Detail_grActivity.this, options);
        } catch (Exception e) {
            Toast.makeText(Fare_Detail_grActivity.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        // After successful payment Razorpay send back a unique id
        Toast.makeText(Fare_Detail_grActivity.this, "Transaction Successful: " + razorpayPaymentID, Toast.LENGTH_LONG).show();

       Log.e("kdljglk","Transaction Successful:"+razorpayPaymentID);

       Intent intent=new Intent(getApplicationContext(),BookingConfirmed.class);
       intent.putExtra(CommonData.RozerPayPaymentId,razorpayPaymentID);
        intent.putExtra(CommonData.TotalAmount,TotalAmount);
        intent.putExtra(CommonData.Type,"Pay");
        intent.putExtra(CommonData.PayAmount,PayAmount);

       startActivity(intent);

        //startActivity(new Intent(Fare_Detail_grActivity.this, ));

    }

    @Override
    public void onPaymentError(int i, String error) {
        // Error message
        Toast.makeText(Fare_Detail_grActivity.this, "Transaction Failed: "+ error , Toast.LENGTH_LONG).show();
    }


}