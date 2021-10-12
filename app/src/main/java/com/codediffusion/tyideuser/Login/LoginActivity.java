package com.codediffusion.tyideuser.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.codediffusion.tyideuser.ApiCalingRetrofit.GlobalClassApiCall;
import com.codediffusion.tyideuser.Extra.CommonData;
import com.codediffusion.tyideuser.Fragment.FragmentForgotPassword;
import com.codediffusion.tyideuser.Mapview.HomeActivity;
import com.codediffusion.tyideuser.R;


import com.codediffusion.tyideuser.databinding.ActivityLoginBinding;
import com.google.gson.Gson;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.codediffusion.tyideuser.Extra.CommonData.UserMobile;
import static com.codediffusion.tyideuser.Extra.CommonData.hideLoading;
import static com.codediffusion.tyideuser.Extra.CommonData.showLoading;


public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding Binding;
    private String Umobile,UPass;
    private CompositeDisposable disposable=new CompositeDisposable();

    private LoginActivity activity;
    private Context context;


    private SharedPreferences sharedPreferences;
    private  SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Binding= DataBindingUtil.setContentView(this,R.layout.activity_login);




       Initialize();
    }

    private void Initialize() {


        context=activity=this;

        sharedPreferences=getSharedPreferences(CommonData.UserData,MODE_PRIVATE);

        editor=sharedPreferences.edit();



        Binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });

        Binding.tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MobileRagisterActivity.class);
                startActivity(intent);
            }
        });

        Binding.tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager=getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fraimLogin,new FragmentForgotPassword()).addToBackStack("").commit();

            }
        });

    }

    private void Validation() {

        Umobile=Binding.etEnterEmailPassword.getText().toString();
        UPass=Binding.etUserPassword.getText().toString();


        if (TextUtils.isEmpty(Umobile)){
            Binding.etEnterEmailPassword.setError("Enter Mobile No");
            Binding.etEnterEmailPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(UPass)){
            Binding.etUserPassword.setError("Enter Password");
            Binding.etUserPassword.requestFocus();
            return;
        }


        CallLoginApi();

    }

    private void CallLoginApi() {
        showLoading(context, "Please Wait");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("login_id", Umobile);
        hashMap.put("password", UPass);


        Log.e("params",hashMap+"");

///

        disposable.add(GlobalClassApiCall.initRetrofit().UserLoginData(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((user, throwatble) -> {
                            hideLoading();
                            if (user != null) {

                                Log.e("jsdrikfhjkfh","Response size: " + new Gson().toJson(user)+"");


                                if (user.getStatus().equals("200")) {

                                    Toast.makeText(activity, user.getMessage()+"", Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(activity, HomeActivity.class);
                                    startActivity(intent);
                                    finishAffinity();


                                    editor.putString(UserMobile,Umobile);
                                    editor.putString(CommonData.UserID,user.getData().getUserId());
                                    editor.apply();
                                    editor.commit();


                                }else{
                                    Toast.makeText(activity, user.getMessage()+"", Toast.LENGTH_SHORT).show();

                                }
                            }else{
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            }

                        }
                )
        );



    }
}