package com.codediffusion.tyideuser.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

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
import com.codediffusion.tyideuser.Mapview.HomeActivity;
import com.codediffusion.tyideuser.R;
import com.codediffusion.tyideuser.databinding.ActivityReagisteredBinding;
import com.google.gson.Gson;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.codediffusion.tyideuser.Extra.CommonData.*;

public class ReagisteredActivity extends AppCompatActivity {
    private ActivityReagisteredBinding binding;
    private String UName;
    private String UMobile;
    private String UEmail;
    private String UPass;
    private String UserID;
    private CompositeDisposable disposable=new CompositeDisposable();
    private SharedPreferences sharedPreferences;
    private  SharedPreferences.Editor editor;

    private ReagisteredActivity activity;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this,R.layout.activity_reagistered);

       context=activity=this;

       sharedPreferences=getSharedPreferences(UserData,MODE_PRIVATE);

       editor=sharedPreferences.edit();

       Intent intent=getIntent();
       if (intent.hasExtra(UserMobile)){
           UMobile=intent.getStringExtra(UserMobile);
         //  UserID=String.valueOf(intent.getIntExtra(CommonData.UserID, Integer.parseInt("")));
            UserID=intent.getStringExtra(CommonData.UserID);


           Log.e("ldjkfk",UserID+"");
           binding.tvUserMobile.setText(UMobile);

       }

        initialize();
    }

    private void initialize() {



      binding.btnRegister.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              VALIDATION();
          }
      });




    }

    private void VALIDATION() {

        UName=binding.etUserName.getText().toString();
        UEmail=binding.etUserEmail.getText().toString();
        UMobile=binding.tvUserMobile.getText().toString();
        UPass=binding.etUserPassword.getText().toString();

        if (TextUtils.isEmpty(UName)){
            binding.etUserName.setError("Enter User Name");
            binding.etUserName.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(UEmail)){
            binding.etUserEmail.setError("Enter Email Id");
            binding.etUserEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(UPass)){
            binding.etUserPassword.setError("Enter Password");
            binding.etUserPassword.requestFocus();
            return;
        }

        if (UPass.length()<4){
            binding.etUserPassword.setText("PassWord Must be 4 Digits");
            binding.etUserPassword.requestFocus();
            return;
        }


        CallRagisterApi();

    }

    private void CallRagisterApi() {

        showLoading(context, "Please Wait");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_mobile", UMobile);
        hashMap.put("user_email", UEmail);
        hashMap.put("user_name", UName);
        hashMap.put("user_password", UPass);
        /*hashMap.put("otp_status", "2");
        hashMap.put("reg_status", "2");*/
        hashMap.put("user_id", UserID);

        Log.e("params",hashMap+"");

///

        disposable.add(GlobalClassApiCall.initRetrofit().UserRegisterData(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((user, throwatble) -> {
                            hideLoading();
                            if (user != null) {

                                Log.e("jksdhkfhk","Response size: " + new Gson().toJson(user)+"");


                                if (user.getStatus().equals("200")) {

                                    Toast.makeText(activity, user.getMessage()+"", Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(activity, HomeActivity.class);
                                    startActivity(intent);
                                    finishAffinity();



                                    editor.putString(UserMobile,UMobile);
                                    editor.putString(UserName,UName);
                                    editor.putString(UserEmail,UEmail);
                                    editor.putString(CommonData.UserID,UserID);
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