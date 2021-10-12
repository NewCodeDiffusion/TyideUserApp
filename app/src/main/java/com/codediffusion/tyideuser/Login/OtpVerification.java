package com.codediffusion.tyideuser.Login;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codediffusion.tyideuser.ApiCalingRetrofit.GlobalClassApiCall;
import com.codediffusion.tyideuser.Extra.CommonData;
import com.codediffusion.tyideuser.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class OtpVerification extends AppCompatActivity implements View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {
    private EditText mPinFirstDigitEditText, mPinSecondDigitEditText, mPinThirdDigitEditText, mPinForthDigitEditText;
    private EditText mPinFifthDigitEditText;
    private EditText mPinHiddenEditText;
    private TextView verifyotp, tv_userMobile,tv_Timer;
    private CheckBox cheackBox;
    ActionBar actionBar;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private OtpVerification activity;
    private Context context;


    private String UOtp, UMobile;


    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void hideSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void init() {
        mPinFirstDigitEditText = (EditText) findViewById(R.id.pin_first_edittext);
        mPinSecondDigitEditText = (EditText) findViewById(R.id.pin_second_edittext);
        mPinThirdDigitEditText = (EditText) findViewById(R.id.pin_third_edittext);
        mPinForthDigitEditText = (EditText) findViewById(R.id.pin_forth_edittext);
        mPinHiddenEditText = (EditText) findViewById(R.id.pin_hidden_edittext);
        cheackBox = (CheckBox) findViewById(R.id.cheackBox);
            tv_Timer = (TextView) findViewById(R.id.tv_Timer);

        tv_userMobile = (TextView) findViewById(R.id.tv_userMobile);


        tv_userMobile.setText(UMobile);


            /////////////StartTimer
            new CountDownTimer(30000, 1000) {
                    public void onTick(long millisUntilFinished) {
                            // tv_Timer.setText(" "+ millisUntilFinished / 1000);


                            tv_Timer.setText("" + String.format("0%d : %d ",
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))+" "+"sec wating for the OTP");

                    }

                    public void onFinish() {
                            tv_Timer.setText("Resend");
                    }
            }.start();



    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MainLayout(OtpVerification.this, null));

        actionBar = getSupportActionBar();
//        actionBar.hide();

        context = activity = this;
        initialize();

        verifyotp = findViewById(R.id.verify);
        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VAlidation();
                //startActivity(new Intent(OtpVerification.this, HomeActivity.class));
            }
        });
        init();
        setPINListeners();
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        final int id = v.getId();
        switch (id) {
            case R.id.pin_first_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_second_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_third_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_forth_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = v.getId();
            switch (id) {
                case R.id.pin_hidden_edittext:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
//                        if (mPinHiddenEditText.getText().length() == 5)
//                            mPinFifthDigitEditText.setText("");
                        if (mPinHiddenEditText.getText().length() == 4)
                            mPinForthDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 3)
                            mPinThirdDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 2)
                            mPinSecondDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 1)
                            mPinFirstDigitEditText.setText("");
                        if (mPinHiddenEditText.length() > 0)
                            mPinHiddenEditText.setText(mPinHiddenEditText.getText().subSequence(0, mPinHiddenEditText.length() - 1));
                        return true;
                    }
                    break;
                default:
                    return false;
            }
        }
        return false;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setDefaultPinBackground(mPinFirstDigitEditText);
        setDefaultPinBackground(mPinSecondDigitEditText);
        setDefaultPinBackground(mPinThirdDigitEditText);
        setDefaultPinBackground(mPinForthDigitEditText);
        setDefaultPinBackground(mPinFifthDigitEditText);

        if (s.length() == 0) {
            setFocusedPinBackground(mPinFirstDigitEditText);
            mPinFirstDigitEditText.setText("");
        } else if (s.length() == 1) {
            setFocusedPinBackground(mPinSecondDigitEditText);
            mPinFirstDigitEditText.setText(s.charAt(0) + "");
            mPinSecondDigitEditText.setText("");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 2) {
            setFocusedPinBackground(mPinThirdDigitEditText);
            mPinSecondDigitEditText.setText(s.charAt(1) + "");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 3) {
            setFocusedPinBackground(mPinForthDigitEditText);
            mPinThirdDigitEditText.setText(s.charAt(2) + "");
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 4) {
            setFocusedPinBackground(mPinFifthDigitEditText);
            mPinForthDigitEditText.setText(s.charAt(3) + "");
        }
    }

    private void setDefaultPinBackground(EditText editText) {
        setViewBackground(editText, getResources().getDrawable(R.drawable.rectangle_grayboder));
    }

    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    private void setFocusedPinBackground(EditText editText) {
        setViewBackground(editText, getResources().getDrawable(R.drawable.rectangle_grayboder));
    }

    private void setPINListeners() {
        mPinHiddenEditText.addTextChangedListener(this);

        mPinFirstDigitEditText.setOnFocusChangeListener(this);
        mPinSecondDigitEditText.setOnFocusChangeListener(this);
        mPinThirdDigitEditText.setOnFocusChangeListener(this);
        mPinForthDigitEditText.setOnFocusChangeListener(this);

        mPinFirstDigitEditText.setOnKeyListener(this);
        mPinSecondDigitEditText.setOnKeyListener(this);
        mPinThirdDigitEditText.setOnKeyListener(this);
        mPinForthDigitEditText.setOnKeyListener(this);
        mPinHiddenEditText.setOnKeyListener(this);
    }

    @SuppressWarnings("deprecation")
    public void setViewBackground(View view, Drawable background) {
        if (view == null || background == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    public class MainLayout extends LinearLayout {

        public MainLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.activity_otp_verification, this);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            final int proposedHeight = MeasureSpec.getSize(heightMeasureSpec);
            final int actualHeight = getHeight();

            Log.d("TAG", "proposed: " + proposedHeight + ", actual: " + actualHeight);

            if (actualHeight >= proposedHeight) {
                // Keyboard is shown
                if (mPinHiddenEditText.length() == 0)
                    setFocusedPinBackground(mPinFirstDigitEditText);
                else
                    setDefaultPinBackground(mPinFirstDigitEditText);
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void initialize() {

        Intent intent = getIntent();
        if (intent.hasExtra(CommonData.UserOtp)) {
            UOtp = intent.getStringExtra(CommonData.UserOtp);
            UMobile = intent.getStringExtra(CommonData.UserMobile);
            // CommonData.CommonLog("userOtp"+UOtp);

            Toast.makeText(this, UOtp + " Your Otp", Toast.LENGTH_LONG).show();

        }

    }

    private void VAlidation() {
        String Inputotp = mPinFirstDigitEditText.getText().toString() + mPinSecondDigitEditText.getText().toString() + mPinThirdDigitEditText.getText().toString() +
                mPinForthDigitEditText.getText().toString();


        if (Inputotp.isEmpty()) {
            Toast.makeText(activity, "Enter Otp", Toast.LENGTH_SHORT).show();

            return;
        }

        if (!cheackBox.isChecked()) {
            Toast.makeText(activity, "Select Term &amp; Condition", Toast.LENGTH_SHORT).show();
            return;
        }

        int USerOtp = Integer.parseInt(UOtp);
        int INPUTOTP = Integer.parseInt(Inputotp);

        if (USerOtp == INPUTOTP) {
            UserVerifyDataApi();
        } else {
            Toast.makeText(activity, "OTP Not Match", Toast.LENGTH_SHORT).show();
        }


    }

    private void UserVerifyDataApi() {

        CommonData.showLoading(context, "Please Wait");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_mobile", UMobile);
        hashMap.put("user_email", "");
        hashMap.put("user_name", "");
        hashMap.put("user_password", "");
        hashMap.put("otp_status", "2");
        //hashMap.put("reg_status", "1");
///

        compositeDisposable.add(GlobalClassApiCall.initRetrofit().RegisterOtpData(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((user, throwatble) -> {
                            CommonData.hideLoading();
                            if (user != null) {

                                Log.e("ljfkhgk", "Response size: " + new Gson().toJson(user) + "");


                                if (user.getStatus().equals("200")) {


                                    int UserID = user.getData().getUserId();
                                    String NewUserID = String.valueOf(UserID);


                                    Toast.makeText(activity, user.getMessage() + "", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(activity, ReagisteredActivity.class);
                                    intent.putExtra(CommonData.UserMobile, UMobile);
                                    intent.putExtra(CommonData.UserID, NewUserID);

                                    Log.e("klfj", NewUserID + "");

                                    startActivity(intent);


                                } else {
                                    Toast.makeText(activity, user.getMessage() + "", Toast.LENGTH_SHORT).show();

                                }
                            }

                        }
                )
        );


    }

}