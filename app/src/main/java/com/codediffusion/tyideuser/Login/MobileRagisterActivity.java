package com.codediffusion.tyideuser.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codediffusion.tyideuser.ApiCalingRetrofit.GlobalClassApiCall;
import com.codediffusion.tyideuser.Extra.CommonData;
import com.codediffusion.tyideuser.R;

import com.codediffusion.tyideuser.databinding.ActivityRegisterBinding;
import com.google.gson.Gson;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MobileRagisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private MobileRagisterActivity activity;
    private Context context;

    private String UMobileNo;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);


        context = activity = this;

        initialize();
//        actionBar.hide();

    }

    private void initialize() {
        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VAlidation();
               // startActivity(new Intent(MobileRagisterActivity.this, OtpVerification.class));
            }
        });


    }

    private void VAlidation() {
        UMobileNo = binding.etUserMobile.getText().toString();

        if (TextUtils.isEmpty(UMobileNo)) {
            binding.etUserMobile.setError("Enter Mobile No");
            binding.etUserMobile.requestFocus();
            return;
        }

        if (UMobileNo.length() < 10) {
            binding.etUserMobile.setError("Mobile No Must be 10 Digits");
            binding.etUserMobile.requestFocus();
            return;
        }

        CallRagisterApi();

    }

    private void CallRagisterApi() {
        CommonData.showLoading(context, "Please Wait");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_mobile", UMobileNo);
        hashMap.put("user_email", "");
        hashMap.put("user_name", "");
        hashMap.put("user_password", "");
        hashMap.put("otp_status", "1");
        hashMap.put("reg_status", "1");

///

        disposable.add(GlobalClassApiCall.initRetrofit().RegisterUserData(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((user, throwatble) -> {
                            CommonData.hideLoading();
                            if (user != null && !user.equals("")) {

                                Log.e("ljfkhgk","Response size: " + new Gson().toJson(user)+"");


                                if (user.getStatus().equals("200")) {

                                    Toast.makeText(activity, user.getMessage()+"", Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(activity,OtpVerification.class);
                                    intent.putExtra(CommonData.UserOtp,user.getData().getOtp());
                                    intent.putExtra(CommonData.UserMobile,UMobileNo);

                                    startActivity(intent);


                                }else{
                                    Toast.makeText(activity, user.getMessage()+"", Toast.LENGTH_SHORT).show();

                                }
                            }else{
                                Toast.makeText(activity, "Mobile Number is allready used", Toast.LENGTH_SHORT).show();

                            }

                        }
                )
        );


    }
}