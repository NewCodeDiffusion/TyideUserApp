package com.codediffusion.tyideuser;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codediffusion.tyideuser.Extra.CommonData;
import com.codediffusion.tyideuser.Login.LoginActivity;
import com.codediffusion.tyideuser.Mapview.HomeActivity;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class SpleshActivity extends AppCompatActivity implements Animation.AnimationListener {

    private static final int REQUEST_CHECK_SETTINGS = 1;
    Animation animFadeIn;
    RelativeLayout linearLayout;
    ActionBar actionBar;
    private String UserID;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        //actionBar.hide();
        sharedPreferences = getSharedPreferences(CommonData.UserData, MODE_PRIVATE);
        if (sharedPreferences.contains(CommonData.UserID)) {
            UserID = sharedPreferences.getString(CommonData.UserID, "");


            Log.e("kldjsfkjf", UserID + "");
        }

        changestatusbarcolor();
        if (Build.VERSION.SDK_INT >= 21) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();

            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

        }

        // load the animation
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.animation_fade_in);
        // set animation listener
        animFadeIn.setAnimationListener(this);
        // animation for image
        linearLayout = (RelativeLayout) findViewById(R.id.layout_linear);
        // start the animation
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.startAnimation(animFadeIn);


        Cheakpermission();
        //  enableGPS();

    }

    private void OpenAlertDialog() {

        final Dialog dialog = new Dialog(SpleshActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setCancelable(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_location_alert);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.transperent);
        window.setGravity(Gravity.CENTER);


        TextView tv_ok = (TextView) dialog.findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (UserID != null && !UserID.isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    Intent i = new Intent(SpleshActivity.this, LoginActivity.class);
                    startActivity(i);
                    finishAffinity();
                }


            }
        });

        dialog.show();


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void changestatusbarcolor() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) ;
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {


    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    protected void enableGPS() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000 * 10);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, locationSettingsResponse -> {
            init();
        });

        task.addOnFailureListener(this, e -> {
            if (e instanceof ResolvableApiException) {
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(SpleshActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    Toast.makeText(this, sendEx.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {


    }


    private void Cheakpermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {


                if (report.areAllPermissionsGranted()) {



                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (UserID != null && !UserID.isEmpty()) {
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                Intent i = new Intent(SpleshActivity.this, LoginActivity.class);
                                startActivity(i);
                                finishAffinity();
                            }


                        }
                    }, 4000);

                   /* if (UserID != null && !UserID.isEmpty()) {


                    }else{
                       // OpenAlertDialog();
                    }
*/

                    // do you work now
                            /*Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "All Permission Granted Successfull", Snackbar.LENGTH_LONG);
                            snackbar.show();*/

                    //enableGPS();


                } else {

                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "All Permission not Granted", Snackbar.LENGTH_LONG);
                    /*//change snackbar background colour
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);*/
                    snackbar.show();

                    finishAffinity();

                    for (int i = 0; i < report.getDeniedPermissionResponses().size(); i++) {
                        Log.d("dennial permision res",
                                report.getDeniedPermissionResponses().get(i).getPermissionName());

                    }
                }
                // check for permanent denial of any permission
                if (report.isAnyPermissionPermanentlyDenied()) {
                    // permission is denied permenantly, navigate user to app settings
                    Toast.makeText(SpleshActivity.this, "Any permission permanently denied", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", getPackageName(), null));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();


            }
        }).check();
    }


}