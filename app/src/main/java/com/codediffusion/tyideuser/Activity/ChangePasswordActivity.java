package com.codediffusion.tyideuser.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codediffusion.tyideuser.ApiCalingRetrofit.GlobalClassApiCall;
import com.codediffusion.tyideuser.Extra.CommonData;
import com.codediffusion.tyideuser.Mapview.HomeActivity;
import com.codediffusion.tyideuser.R;
import com.codediffusion.tyideuser.databinding.ActivityChangePasswordBinding;
import com.google.gson.Gson;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.codediffusion.tyideuser.Extra.CommonData.hideLoading;
import static com.codediffusion.tyideuser.Extra.CommonData.showLoading;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding binding;
    private String UPass,UReEnterPass,UOldPass,USERID;
    private CompositeDisposable disposable=new CompositeDisposable();

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this,R.layout.activity_change_password);

        sharedPreferences=getSharedPreferences(CommonData.UserData,MODE_PRIVATE);
        if (sharedPreferences.contains(CommonData.UserID)){
            USERID=sharedPreferences.getString(CommonData.UserID,"");
        }


        initialization();

    }

    private void initialization() {

        binding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Validation();
            }
        });

       binding.ivBack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });

    }

    private void Validation() {
        UPass=binding.etPassword.getText().toString();
        UReEnterPass=binding.etRePassword.getText().toString();
        UOldPass=binding.etOldPassword.getText().toString();

        if (TextUtils.isEmpty(UOldPass)){
            binding.etOldPassword.setError("Enter Old Password");
            binding.etOldPassword.requestFocus();
            return;
        }
        if (UOldPass.length()<4){
            binding.etOldPassword.setError("Old Password Must be 4 Digits");
            binding.etOldPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(UPass)){
            binding.etPassword.setError("Enter New Password");
            binding.etPassword.requestFocus();
            return;
        }
        if (UPass.length()<4){
            binding.etPassword.setError("New Password Must be 4 Digits");
            binding.etPassword.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(UReEnterPass)){
            binding.etRePassword.setError("Enter RE-Enter Password");
            binding.etRePassword.requestFocus();
            return;
        }

        if (UReEnterPass.length()<4){
            binding.etRePassword.setError("Re-Password Must be 4 Digits");
            binding.etRePassword.requestFocus();
            return;
        }

        if (!UPass.equalsIgnoreCase(UReEnterPass)){
            Toast.makeText(this, "PassWord Or Confirm Password Not Match", Toast.LENGTH_SHORT).show();
            return;
        }


        CallChangePasswordApi();

    }

    private void CallChangePasswordApi() {

        showLoading(ChangePasswordActivity.this, "Please Wait");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", USERID);
        hashMap.put("current_password", UOldPass);
        hashMap.put("password", UPass);
        hashMap.put("confirm_password", UReEnterPass);



        Log.e("params",hashMap+"");

///

        disposable.add(GlobalClassApiCall.initRetrofit().ChangePassData(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((user, throwatble) -> {
                            hideLoading();
                            if (user != null) {

                                Log.e("jsdrikfhjkfh","Response size: " + new Gson().toJson(user)+"");


                                if (user.getStatus().equals("200")) {

                                    Toast.makeText(getApplicationContext(), user.getMessage()+"", Toast.LENGTH_LONG).show();


                                    Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(intent);



                                }else{
                                    Toast.makeText(getApplicationContext(), user.getMessage()+"", Toast.LENGTH_SHORT).show();

                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
                            }

                        }
                )
        );


    }
}