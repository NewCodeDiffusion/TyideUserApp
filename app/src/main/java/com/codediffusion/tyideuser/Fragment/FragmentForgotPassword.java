package com.codediffusion.tyideuser.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codediffusion.tyideuser.ApiCalingRetrofit.GlobalClassApiCall;
import com.codediffusion.tyideuser.Login.LoginActivity;
import com.codediffusion.tyideuser.R;
import com.google.gson.Gson;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.codediffusion.tyideuser.Extra.CommonData.hideLoading;
import static com.codediffusion.tyideuser.Extra.CommonData.showLoading;

public class FragmentForgotPassword extends Fragment {
    private EditText et_MobileNo;
    private TextView btn_Submit;
    private String UMobile;
    private CompositeDisposable disposable=new CompositeDisposable();



    public FragmentForgotPassword() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_forgot_password, container, false);

         initialize(view);
    return view;
    }

    private void initialize(View view) {

        et_MobileNo=view.findViewById(R.id.et_MobileNo);
        btn_Submit=view.findViewById(R.id.btn_Submit);


        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });


    }

    private void Validation() {

        UMobile=et_MobileNo.getText().toString();

        if (TextUtils.isEmpty(UMobile)){
            et_MobileNo.setError("Enter Mobile No");
            et_MobileNo.requestFocus();
            return;
        }

        if (UMobile.length()<10){
            et_MobileNo.setError("Mobile No Must be 10 Digits");
            et_MobileNo.requestFocus();
            return;
        }



        CallForgotPassWordApi();

    }

    private void CallForgotPassWordApi() {

        showLoading(getContext(), "Please Wait");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("mobile", UMobile);



        Log.e("params",hashMap+"");

///

        disposable.add(GlobalClassApiCall.initRetrofit().ForgotPassData(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((user, throwatble) -> {
                            hideLoading();
                            if (user != null) {

                                Log.e("jsdrikfhjkfh","Response size: " + new Gson().toJson(user)+"");


                                if (user.getStatus().equals("200")) {

                                    Toast.makeText(getActivity(), user.getMessage()+"", Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(getContext(), LoginActivity.class);
                                    startActivity(intent);
                                    getActivity().finishAffinity();




                                }else{
                                    Toast.makeText(getActivity(), user.getMessage()+"", Toast.LENGTH_SHORT).show();

                                }
                            }else{
                                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                            }

                        }
                )
        );


    }
}