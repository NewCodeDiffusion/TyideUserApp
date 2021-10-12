package com.codediffusion.tyideuser.Mapview;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.codediffusion.tyideuser.Activity.ChangePasswordActivity;
import com.codediffusion.tyideuser.Activity.MyAccountActivity;
import com.codediffusion.tyideuser.Activity.Wallet;
import com.codediffusion.tyideuser.AllBooking.AllBookingActivity;
import com.codediffusion.tyideuser.ApiCalingRetrofit.GlobalClassApiCall;
import com.codediffusion.tyideuser.BookingSummaryAndInvoiceActivity;
import com.codediffusion.tyideuser.Extra.CommonData;
import com.codediffusion.tyideuser.Fragment.WebViewFragmentUrl;
import com.codediffusion.tyideuser.Login.LoginActivity;
import com.codediffusion.tyideuser.R;
import com.codediffusion.tyideuser.adapter.AdapterSearchDropLocationData;
import com.codediffusion.tyideuser.adapter.AdapterSearchFromLocationData;
import com.codediffusion.tyideuser.adapter.AdapterSelectMultipleCity;
import com.codediffusion.tyideuser.adapter.AdapterSelectPackage;
import com.codediffusion.tyideuser.model.modelSelectPackage;
import com.codediffusion.tyideuser.model.modelToAndFromCityDataApi;
import com.codediffusion.tyideuser.search_autocomplete.PlacesAutoCompleteAdapter;
import com.codediffusion.tyideuser.search_autocomplete.Prediction;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.codediffusion.tyideuser.Extra.CommonData.UserData;
import static com.codediffusion.tyideuser.Extra.CommonData.hideLoading;
import static com.codediffusion.tyideuser.Extra.CommonData.showLoading;

public class HomeActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, AdapterView.OnItemSelectedListener, AdapterSearchFromLocationData.SelectFromLocation, AdapterSearchDropLocationData.SelectDropLocation,AdapterSelectPackage.SelectPackage {
    private LinearLayout LL_AllBooking, ll_profile, signout, ll_trip,ll_ChangePassword,ll_privacyPolice,ll_selectPackage,
            ll_selectTripType,ll_share,ll_Account;
    private GoogleMap mMap;
    private CompositeDisposable disposable = new CompositeDisposable();
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private LinearLayout li_oneway, ll_roundtrip, ll_dropDate, ll_pickUpDate,ll_pickupTime;
    private TextView oneway, roundtrip, tv_UserName, tv_UserMobile, tv_PickupDate, tv_dropDate,tv_SelectPackage;
    private ImageView sideMenu, profile_image, iv_cross, iv_crossdrop,call;
    private TextView confirm,tv_SearchCar;
    // private EditText et_userCurrentLocation, et_dropLocation, selectedEditText;
    private EditText et_userCurrentLocation, et_dropLocation;
    private Dialog slideDialog;

    private HomeActivity activity;
    private Context context;
    private String UserID, Type, TimeFormat, pickupData, dropData,firstCiteyNAme, SecondCiteyNAme, NewDistance,
            TripFor,SelectTime,firstDate,secondDate,YourTripType="",TripType,PACKAGEID,PACKAGENAME ,
            SelectCarModel,UCarType,UCarModel,UPackageid;
    private RadioButton rb_Outstation, rb_local, rb_Airport;

    ////SearchListData
    private RecyclerView rv_searchList, rv_searchListDropLocation,rv_selectPackage;
    private AdapterSearchFromLocationData adapterSearchFromLocationData;
    private List<modelToAndFromCityDataApi.CityList> Searchlistpickup;
    private List<modelToAndFromCityDataApi.CityList> SearchlistDrop;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static double latitude, longitude;
    private Calendar myCalendar;


    private  String UPickLocation,UDropLocation,UPickUpDate,UDropDate,UPickUpTime,SelectCarType;

    /////Current Time
    private Spinner sp_SelectCurrentTime,sp_SelectTripType,sp_SelectCarType,sp_SelectCarModel;
    private ArrayList<String> timeFormatCurrent;

    private String[] timeFormatNextDay = {"12:00 AM", "12:30 AM", "01:00 AM", "01:30 AM", "02:00 AM", "02:30 AM", "03:00 AM", "03:30 AM", "04:00 AM", "04:30 AM", "05:00 AM", "05:30 AM", "06:00 AM", "06:30 AM", "07:00 AM", "07:30 AM",
            "08:00 AM", "08:30 AM", "09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "12:00 AM", "12:30 PM", "01:00 PM", "01:30 PM", "02:00 PM", "02:30 PM", "03:00 PM", "03:30 PM",
            "04:00 PM", "04:30 PM", "05:00 PM", "05:30 PM", "06:00 PM", "06:30 PM", "07:00 PM", "07:30 PM", "08:00 PM", "08:30 PM", "09:00 PM", "09:30 PM", "10:00 PM", "10:30 PM", "11:00 PM", "11:30 PM"};

    private String[] selectPackage = {"Select Package", "2 Hours,20 Km", "4 Hours,40 Km", "5 Hours,50 Km","6 Hours,70 Km","7 Hours,80 Km"};

    private String[] selectTripType = {"Select Trip Type", "Cab From the Airport", "Cab To the Airport"};
    private String[] selectCarType = {"Manual", "Automatic"};
    private String[] selectCarModel = {"Hatchback", "Sedan","Suv"};

    DateFormat sdf;
    private AutoCompleteTextView tv_userCurrentLocation,tv_dropLocation;


    ///////Select Multiple Search Data///////////
    private RecyclerView rv_SelectMultiCity;
    private AdapterSelectMultipleCity adapterSelectMultipleCity;
    private ArrayList<String> multipleCityList = new ArrayList<>();


    ////Select PAckage

    private AdapterSelectPackage adapterSelectPackage;
    private List<modelSelectPackage.Datum>packageListData=new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = activity = this;

        sharedPreferences = getSharedPreferences(UserData, MODE_PRIVATE);

        editor = sharedPreferences.edit();
        if (sharedPreferences.contains(CommonData.UserID)) {
            UserID = sharedPreferences.getString(CommonData.UserID, "");
        }


        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions();

        }


    }

    private void initialization() {
        ll_share = findViewById(R.id.ll_share);
        ll_selectTripType = findViewById(R.id.ll_selectTripType);
        ll_selectPackage = findViewById(R.id.ll_selectPackage);
        sp_SelectTripType = findViewById(R.id.sp_SelectTripType);
        sp_SelectCarModel = findViewById(R.id.sp_SelectCarModel);
        sp_SelectCarType = findViewById(R.id.sp_SelectCarType);
        tv_SelectPackage = findViewById(R.id.tv_SelectPackage);
        rv_SelectMultiCity = findViewById(R.id.rv_SelectMultiCity);
        tv_userCurrentLocation = findViewById(R.id.tv_userCurrentLocation);
        tv_dropLocation = findViewById(R.id.tv_dropLocation);
        ll_ChangePassword = findViewById(R.id.ll_ChangePassword);
        ll_Account = findViewById(R.id.ll_Account);
        tv_SearchCar = findViewById(R.id.tv_SearchCar);
        call = findViewById(R.id.call);
        sp_SelectCurrentTime = findViewById(R.id.sp_SelectCurrentTime);
        ll_pickupTime = findViewById(R.id.ll_pickupTime);
        ll_pickUpDate = findViewById(R.id.ll_pickUpDate);
        ll_dropDate = findViewById(R.id.ll_dropDate);
        tv_dropDate = findViewById(R.id.tv_dropDate);
        tv_PickupDate = findViewById(R.id.tv_PickupDate);
        iv_crossdrop = findViewById(R.id.iv_crossdrop);
        rv_selectPackage = findViewById(R.id.rv_selectPackage);

        // rv_searchListDropLocation = findViewById(R.id.rv_searchListDropLocation);
        //  rv_searchList = findViewById(R.id.rv_searchList);

        iv_cross = findViewById(R.id.iv_cross);
        ll_trip = findViewById(R.id.ll_trip);
        rb_Airport = findViewById(R.id.rb_Airport);
        rb_Outstation = findViewById(R.id.rb_Outstation);
        signout = findViewById(R.id.signout);
        ll_profile = findViewById(R.id.ll_profile);
        LL_AllBooking = findViewById(R.id.LL_AllBooking);
        li_oneway = (LinearLayout) findViewById(R.id.li_oneway);
        ll_roundtrip = (LinearLayout) findViewById(R.id.ll_roundtrip);
        tv_UserMobile = findViewById(R.id.tv_UserMobile);

        oneway = (TextView) findViewById(R.id.oneway);
        roundtrip = (TextView) findViewById(R.id.roundtrip);
        tv_UserName = findViewById(R.id.tv_UserName);

        li_oneway.setOnClickListener(this);
        ll_roundtrip.setOnClickListener(this);
        rb_local = findViewById(R.id.rb_local);
//drawer layout code open and closed
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer);

        profile_image = findViewById(R.id.profile_image);
        ll_privacyPolice = findViewById(R.id.ll_privacyPolice);


        myCalendar = Calendar.getInstance();


        //sp_SelectCarType.setOnItemSelectedListener(this);
        //sp_SelectCarModel.setOnItemSelectedListener(this);




        sideMenu = findViewById(R.id.sideMenu);
        sideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
            }
        });


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:8279766758"));
                        startActivity(intent);
                    }
                });

            }
        });


        rb_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ll_trip.setVisibility(View.GONE);
                ll_dropDate.setVisibility(View.GONE);
                TripFor = "1";
                ll_selectPackage.setVisibility(View.VISIBLE);
                ll_selectTripType.setVisibility(View.GONE);
                Log.e("dkjfjk", TripFor + "");
                tv_SelectPackage.setText("");
            }
        });
        rb_Airport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_trip.setVisibility(View.GONE);
                ll_dropDate.setVisibility(View.GONE);
                TripFor = "4";
                ll_selectPackage.setVisibility(View.GONE);
                ll_selectTripType.setVisibility(View.VISIBLE);
                Log.e("dkjfjk", TripFor + "");
            }
        });
        rb_Outstation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_trip.setVisibility(View.VISIBLE);
                //ll_selectPackage.setVisibility(View.GONE);
                ll_selectPackage.setVisibility(View.VISIBLE);
                ll_selectTripType.setVisibility(View.GONE);
                TripFor = "2";

                tv_SelectPackage.setText("");
                Log.e("dkjfjk", TripFor + "");
            }
        });

        LL_AllBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AllBookingActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(navigationView);
            }
        });

        ll_privacyPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fraimMain, new WebViewFragmentUrl()).addToBackStack("").commit();


                drawerLayout.closeDrawer(navigationView);
            }
        });


        ll_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Wallet.class);
                startActivity(intent);

                drawerLayout.closeDrawer(navigationView);
            }
        });


        ll_ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                startActivity(intent);

                drawerLayout.closeDrawer(navigationView);
            }
        });





        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyAccountActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(navigationView);
            }
        });


        ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent =   new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"");
                String app_url = " https://play.google.com/store/apps/details?id="+getPackageName();
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Hey Checkout My App At "+app_url);
                startActivity(Intent.createChooser(shareIntent, "Share via"));

                drawerLayout.closeDrawer(navigationView);
            }
        });

       /* if (et_userCurrentLocation.getText().length()>0){
            iv_cross.setVisibility(View.VISIBLE);
        }else{
            iv_cross.setVisibility(View.GONE);

        }*/
        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // et_userCurrentLocation.setText("");
                tv_userCurrentLocation.setText("");
            }
        });
        iv_crossdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_dropLocation.setText("");
                // multipleCityList.clear();
                // rv_SelectMultiCity.setVisibility(View.GONE);
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(HomeActivity.this)
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);

                                editor.clear();
                                editor.apply();

                                drawerLayout.closeDrawers();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                drawerLayout.closeDrawers();
            }
        });

        ////////////////Change Qasim Data//////////////////////////

        //loadData();

        ///////////////////////////

        //getProfileData();

        tv_userCurrentLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().equals("")) {
                    //rv_searchList.setVisibility(View.GONE);
                    iv_cross.setVisibility(View.GONE);
                } else {
                    //   rv_searchList.setVisibility(View.VISIBLE);
                    iv_cross.setVisibility(View.VISIBLE);


                }
                loadData();
                //  LoadSearchApiData(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

/*
        et_userCurrentLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().equals("")) {
                    rv_searchList.setVisibility(View.GONE);
                    iv_cross.setVisibility(View.GONE);
                } else {
                    rv_searchList.setVisibility(View.VISIBLE);
                    iv_cross.setVisibility(View.VISIBLE);


                }
               // String apiKey = getString(R.string.apiKe);
                if (!Places.isInitialized()) {
                    Places.initialize(getApplicationContext(), apikey);
                }

                // Create a new Places client instance.
                placesClient = Places.createClient(HomeActivity.this);
                //  LoadSearchApiData(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/

        tv_dropLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().equals("")) {
                    //  rv_searchListDropLocation.setVisibility(View.GONE);
                    iv_crossdrop.setVisibility(View.GONE);
                } else {
                    // rv_searchListDropLocation.setVisibility(View.VISIBLE);
                    iv_crossdrop.setVisibility(View.VISIBLE);


                }


                loadDropData();

                /*if (YourTripType.equalsIgnoreCase("RoundTrip")) {
                    LoadRoundTripDropListData();
                } else {
                    loadDropData();
                }*/


                //  LoadSearchApiDataDropLocation(s);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ll_pickUpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Type = "pick";
                datePickerDialog(Type);


            }
        });


        ll_dropDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Type = "drop";
                datePickerDialog(Type);
            }
        });
////By Defalt Set One Way Trip And Out Station

        li_oneway.setBackgroundResource(R.drawable.rectangle_bluecornnor);
        ll_roundtrip.setBackgroundResource(R.drawable.rectangle_whiterounded);
        ll_trip.setVisibility(View.VISIBLE);
        oneway.setTextColor(Color.parseColor("#ffffff"));
        roundtrip.setTextColor(Color.parseColor("#919ca5"));
        // ll_dropDate.setVisibility(View.GONE);
         TripType="1";
        TripFor = "1";
        Log.e("dkjfjk", TripFor + "");

        rb_Outstation.setChecked(true);


        tv_SearchCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });






/*// sp_SelectPackage.setOnItemSelectedListener(this);
        ArrayAdapter SelectPackageType = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, selectPackage);
        SelectPackageType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_SelectPackage.setAdapter(SelectPackageType);*/

// sp_SelectPackage.setOnItemSelectedListener(this);
        ArrayAdapter SelectTripType = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, selectTripType);
        SelectTripType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_SelectTripType.setAdapter(SelectTripType);







        sp_SelectCarType.setOnItemSelectedListener(this);
        ArrayAdapter SelectCar = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, selectCarType);
        SelectCar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_SelectCarType.setAdapter(SelectCar);

        sp_SelectCarType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectCarType = sp_SelectCarType.getItemAtPosition(sp_SelectCarType.getSelectedItemPosition()).toString();
                Log.e("hjhjhjhj", SelectCarType + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






        sp_SelectCarModel.setOnItemSelectedListener(this);
        ArrayAdapter CarModel = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, selectCarModel);
        CarModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_SelectCarModel.setAdapter(CarModel);


        sp_SelectCarModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectCarModel = sp_SelectCarModel.getItemAtPosition(sp_SelectCarModel.getSelectedItemPosition()).toString();
                Log.e("hjhjhjhj", SelectCarModel + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });















        tv_SelectPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadPackageList();
            }
        });





    }


    private void LoadTimeSlotData() {

        timeFormatCurrent = new ArrayList<>();

        timeFormatCurrent.clear();

        String formatTime = "hh:mm a";
        DateFormat sdfTime = new SimpleDateFormat(formatTime, Locale.US);
        ;

        // String firstDate = "24/03/2021";
        firstDate = tv_PickupDate.getText().toString();
        Log.e("khlgkghl", firstDate + "");

        long NEWTime = System.currentTimeMillis() + 7200000;

        Time CurrentTime = new Time(NEWTime);
        //Time CurrentTime=new Time(System.currentTimeMillis());
        //String CurrentTime= String.valueOf(System.currentTimeMillis());
        Log.e("kmfjh", CurrentTime + "");

        String newTime = sdfTime.format(CurrentTime);
        Log.e("lkdslfjkfk", newTime + "");


        // String firstTime = "12:00 AM";
        String firstTime = newTime;
        // String secondDate = "25/03/2021";

        if (YourTripType.equalsIgnoreCase("RoundTrip")) {
            secondDate = tv_dropDate.getText().toString();
            Log.e("jkgjk", secondDate + "");
        } else {
            secondDate = firstDate;
            Log.e("jkgjk", secondDate + "");
        }


        Log.e("jfgjkghj", secondDate + "");
        String secondTime = "11:59 PM";

        String format = "dd-MM-yyyy hh:mm a";


        sdf = new SimpleDateFormat(format, Locale.US);
        Date dateObj1 = null;
        try {

            dateObj1 = sdf.parse(firstDate + " " + firstTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date dateObj2 = null;
        try {
            dateObj2 = sdf.parse(secondDate + " " + secondTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        long dif = dateObj1.getTime();

        while (dif < dateObj2.getTime()) {

            Time time = new Time(dif);
            Log.e("kdjkhfj", time + "");


            TimeFormat = sdfTime.format(time);

            Log.e("lkhklgkolo", TimeFormat + "");


            timeFormatCurrent.add(TimeFormat);
            Log.e("fkjgkhfr", timeFormatCurrent + "");


            dif += 1800000;
        }


       /* timeFormatCurrent.add(TimeFormat);
        Log.e("fkjgkhfr",timeFormatCurrent+"");*/

       /* int pickUpDate= Integer.parseInt(firstDate);
        int DropDate= Integer.parseInt(secondDate);*/

        // if (tv_PickupDate.getText().toString().equalsIgnoreCase(tv_dropDate.getText().toString())) {
        if (firstDate.equalsIgnoreCase(secondDate)) {

            Log.e("ljdfkj", "Success");
            sp_SelectCurrentTime.setOnItemSelectedListener(this);
            ArrayAdapter TimeFormet = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, timeFormatCurrent);
            TimeFormet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_SelectCurrentTime.setAdapter(TimeFormet);





        } else {
            sp_SelectCurrentTime.setOnItemSelectedListener(this);
            ArrayAdapter TimeFormet = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, timeFormatNextDay);
            TimeFormet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_SelectCurrentTime.setAdapter(TimeFormet);




        }


    }


    private void datePickerDialog(String type) {

        // myCalendar.add(Calendar.MONTH, 0);                //Goes 10 Year Back in time ^^

        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {

            // TODO Auto-generated method stub


            myCalendar.set(Calendar.YEAR, year);

            myCalendar.set(Calendar.MONTH, monthOfYear);

            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel(type);

        };

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            DatePickerDialog datePickerDialog = new DatePickerDialog(activity, date

                    , myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)
            );

            long upperLimit = myCalendar.getTimeInMillis();

            // datePickerDialog.getDatePicker().setMaxDate(upperLimit);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();


        }


    }

    private void updateLabel(String type) {

        String myFormat = "dd-MM-yyyy"; //In which you need put here

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (type.equalsIgnoreCase("pick")) {
            tv_PickupDate.setText(sdf.format(myCalendar.getTime()));
            // ll_pickUpDate.setVisibility(View.VISIBLE);
            SetDataPickUpAndDrop();

        } else {
            tv_dropDate.setText(sdf.format(myCalendar.getTime()));

            SetDataPickUpAndDrop();
        }


    }

    private void SetDataPickUpAndDrop() {
        //  if (tv_PickupDate.getText().toString().equalsIgnoreCase(""))
        pickupData = tv_PickupDate.getText().toString();
        dropData = tv_dropDate.getText().toString();

        Log.e("lfkgljg", pickupData + "");
        Log.e("gfjkjirto", dropData + "");

        if (YourTripType.equalsIgnoreCase("RoundTrip")) {
            ll_dropDate.setVisibility(View.VISIBLE);

            if (pickupData.equalsIgnoreCase("Select PickUp Date") || dropData.equalsIgnoreCase("Select Drop Date")) {
                Toast.makeText(activity, "Select  Drop Date", Toast.LENGTH_SHORT).show();

                //  ll_pickupTime.setVisibility(View.GONE);
            } else {
                LoadTimeSlotData();
                Log.e("ldkflhgk", "Done");
                ll_pickupTime.setVisibility(View.VISIBLE);
            }
        } else {
            LoadTimeSlotData();
            Log.e("ldkflhgk", "Done");
            ll_pickupTime.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //create Hazards
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.li_oneway:
                li_oneway.setBackgroundResource(R.drawable.rectangle_bluecornnor);
                ll_roundtrip.setBackgroundResource(R.drawable.rectangle_whiterounded);

                oneway.setTextColor(Color.parseColor("#ffffff"));
                roundtrip.setTextColor(Color.parseColor("#919ca5"));
                //  ll_dropDate.setVisibility(View.GONE);
                ll_dropDate.setVisibility(View.GONE);
                TripType="1";
                //TripFor = "1";
                YourTripType = "OneWay";
                Log.e("dkjfjk", TripFor + "");


                tv_dropLocation.setText("");
                tv_SelectPackage.setText("");
                tv_userCurrentLocation.setText("");
                multipleCityList.clear();

                break;

            case R.id.ll_roundtrip:
                li_oneway.setBackgroundResource(R.drawable.rectangle_whiterounded);
                ll_roundtrip.setBackgroundResource(R.drawable.rectangle_bluecornnor);

                oneway.setTextColor(Color.parseColor("#919ca5"));
                roundtrip.setTextColor(Color.parseColor("#ffffff"));

                ll_dropDate.setVisibility(View.VISIBLE);

               // TripFor = "2";
                  TripType="2";


                YourTripType = "RoundTrip";
                Log.e("dkjfjk", TripFor + "");
                rv_SelectMultiCity.setVisibility(View.VISIBLE);
                LoadRoundTripDropListData();

                tv_SelectPackage.setText("");
                tv_dropLocation.setText("");
                tv_userCurrentLocation.setText("");
                multipleCityList.clear();


                break;
        }


        if (YourTripType.equalsIgnoreCase("RoundTrip")) {
            // iv_AddLocation.setVisibility(View.VISIBLE);

            iv_crossdrop.setVisibility(View.GONE);
        } else {
            // iv_AddLocation.setVisibility(View.GONE);

            iv_crossdrop.setVisibility(View.VISIBLE);
        }


    }


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String p : CommonData.Location_Permision) {
                result = ContextCompat.checkSelfPermission(activity, p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                    initialization();
                }
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((activity),
                    listPermissionsNeeded.toArray(new
                            String[listPermissionsNeeded.size()]), 100);


            return false;
        } else {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);

            MapsInitializer.initialize(activity);
            displayLocation();

            initialization();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);

                mapFragment.getMapAsync(this);
                MapsInitializer.initialize(activity);
                displayLocation();
                initialization();
            } else {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);

                mapFragment.getMapAsync(this);
                MapsInitializer.initialize(activity);
                displayLocation();


                // initialization();
            }
            return;
        }
    }

    Geocoder geocoder;
    List<Address> addresses;

    private void displayLocation() {
        final LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


                if (network_enabled) {


                    if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    final Location location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if (location != null) {

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.e("coordinates", longitude + ",lati" + latitude + "");


/////get Address
                        geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());

                        try {

                            addresses = geocoder.getFromLocation(latitude, longitude, 1);


                            if (!addresses.isEmpty()) {
                                Address returnedAddress = addresses.get(0);
                                StringBuilder strReturnedAddress = new StringBuilder("");

                                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
                                }


                                Log.e("MyCurrentLoctionAddress", "" + strReturnedAddress.toString());

                                String getLocation = addresses.get(0).getLocality();
                                // et_userCurrentLocation.setText(getLocation);


                            } else {

                                //      Log.e("MyCurrentLoctionAddress", "No Address returned!");

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

//////
                        final LatLng current_Mark = new LatLng(latitude, longitude);

                        Log.e("dklfjk",latitude+""+longitude);


                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(current_Mark)
                                .zoom(15)
                                .build();
                        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        mMap.animateCamera(camUpd3);


                        Circle circle = mMap.addCircle(new CircleOptions()
                                .center(new LatLng(latitude, longitude))
                                .radius(100)
                                .strokeColor(Color.RED)
                                .fillColor(Color.BLUE));


                    }
                }

            }
        }, 1000);
    }


    private void getProfileData() {
        // showLoading(context, "Please Wait");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", UserID);

        Log.e("params", hashMap + "");

///

        disposable.add(GlobalClassApiCall.initRetrofit().GetProfileData(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((user, throwatble) -> {
                            hideLoading();
                            if (user != null) {

                                Log.e("jksdhkfhk", "Response size: " + new Gson().toJson(user) + "");


                                if (user.getStatus().equals("200")) {

                                    // Toast.makeText(activity, user.getMessage() + "", Toast.LENGTH_LONG).show();


                                    tv_UserName.setText(user.getData().getName());
                                    tv_UserMobile.setText(user.getData().getMobile());

                                    if (user.getData().getPicture().equalsIgnoreCase("http://truecabs.in//upload/")) {

                                        RequestOptions requestOptions = new RequestOptions();
                                        requestOptions.placeholder(R.drawable.ic_man_1);
                                        requestOptions.error(R.drawable.ic_man_1);
                                        Glide.with(context).setDefaultRequestOptions(requestOptions).load(R.drawable.ic_man_1).
                                                diskCacheStrategy(DiskCacheStrategy.ALL).into(profile_image);
                                    } else {
                                        Glide.with(context).load(user.getData().getPicture()).into(profile_image);

                                    }

                                } else {
                                    // Toast.makeText(activity, user.getMessage() + "", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            }

                        }
                )
        );

    }





    ////StartSearchDataForGoogleMapLocation

    private void LoadSearchApiData(CharSequence serchKEy) {
        Searchlistpickup = new ArrayList<>();
        Searchlistpickup.clear();


        //  CommonData.showLoading(context, "Please Wait");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("search_by", serchKEy);
        hashMap.put("type", "1");

        Log.e("paramsjgkjgkjh", hashMap + "");

///

        disposable.add(GlobalClassApiCall.initRetrofit().GetSerchData(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((user, throwatble) -> {
                            hideLoading();
                            if (user != null) {
                                //  rv_searchList.setVisibility(View.VISIBLE);
                                Log.e("jksdhkfhk", "Search: " + new Gson().toJson(user) + "");


                                if (user.getStatus().equals("200")) {

                                    // Toast.makeText(activity, user.getMessage() + "", Toast.LENGTH_LONG).show();
                                    for (int i = 0; i < user.getData().getCityList().size(); i++) {
                                        Searchlistpickup.add(user.getData().getCityList().get(i));
                                    }


                                    adapterSearchFromLocationData = new AdapterSearchFromLocationData(Searchlistpickup, getApplicationContext(), HomeActivity.this);

                                    rv_searchList.setAdapter(adapterSearchFromLocationData);
                                    adapterSearchFromLocationData.notifyDataSetChanged();
                                }
                            }

                        }
                )
        );


    }


    private void LoadSearchApiDataDropLocation(CharSequence s) {

        SearchlistDrop = new ArrayList<>();
        SearchlistDrop.clear();


        //  CommonData.showLoading(context, "Please Wait");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("search_by", s);
        hashMap.put("type", "2");

        Log.e("paramsjgkjgkjh", hashMap + "");

///

        disposable.add(GlobalClassApiCall.initRetrofit().GetSerchData(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((user, throwatble) -> {
                            hideLoading();
                            if (user != null) {
                                //  rv_searchList.setVisibility(View.VISIBLE);
                                Log.e("jksdhkfhk", "Search: " + new Gson().toJson(user) + "");


                                if (user.getStatus().equals("200")) {

                                    //     Toast.makeText(activity, user.getMessage() + "", Toast.LENGTH_LONG).show();
                                    for (int i = 0; i < user.getData().getCityList().size(); i++) {
                                        SearchlistDrop.add(user.getData().getCityList().get(i));
                                    }


                                    AdapterSearchDropLocationData adapterSearchDropLocationData = new AdapterSearchDropLocationData(SearchlistDrop, getApplicationContext(), HomeActivity.this);

                                    rv_searchListDropLocation.setAdapter(adapterSearchDropLocationData);
                                    adapterSearchDropLocationData.notifyDataSetChanged();
                                }
                            }

                        }
                )
        );


    }


    @Override
    protected void onResume() {
        super.onResume();

        getProfileData();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //String selectItem=sp_SelectCurrentTime.getSelectedItem().toString();
        SelectTime = sp_SelectCurrentTime.getItemAtPosition(sp_SelectCurrentTime.getSelectedItemPosition()).toString();
        Log.e("fklgklgkg", SelectTime + "");




    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void ChangeLocation(String FromLocation) {

        tv_userCurrentLocation.setText(FromLocation);
        rv_searchList.setVisibility(View.GONE);

        ////HideKey Board
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tv_userCurrentLocation.getWindowToken(), 0);




        if (tv_userCurrentLocation.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Select Pickup Location", Toast.LENGTH_SHORT).show();
        } else {
            firstCiteyNAme = tv_userCurrentLocation.getText().toString();
        }


    }

    @Override
    public void ChangeDropLocation(String dropLocation) {
        tv_dropLocation.setText(dropLocation);

        Log.e("dkflkjfkl",dropLocation+"");
        // rv_searchListDropLocation.setVisibility(View.GONE);

        ////HideKey Board
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tv_dropLocation.getWindowToken(), 0);

        if (tv_dropLocation.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Enter Drop Location", Toast.LENGTH_SHORT).show();

        } else {
            // SecondCiteyNAme = tv_dropLocation.getText().toString();
            SecondCiteyNAme = dropLocation;
            Log.e("dkflkjfkl",SecondCiteyNAme+"Second");
        }

    }






    private void Validation() {

        UPickLocation=tv_userCurrentLocation.getText().toString();
        UDropLocation=tv_dropLocation.getText().toString();
        SecondCiteyNAme=tv_dropLocation.getText().toString();
        // UPickUpDate=tv_PickupDate.getText().toString();
        UPickUpDate=tv_PickupDate.getText().toString();
        UDropDate=tv_dropDate.getText().toString();
        UCarType=SelectCarType;
        UCarModel=SelectCarModel;
        UPickUpTime=SelectTime;
        UPackageid=tv_SelectPackage.getText().toString();

        if (UPackageid.equalsIgnoreCase("Select Package")){
            Toast.makeText(activity, "Select Package", Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(UPickLocation)){
            tv_userCurrentLocation.setError("Enter PicUp Location");
            tv_userCurrentLocation.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(UDropLocation)){
            tv_dropLocation.setError("Enter Drop Location");
            tv_dropLocation.requestFocus();
            return;
        }



        if (UPickUpDate.equalsIgnoreCase("Select PickUp Date")){
            Toast.makeText(activity, "Select PickUp Date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (YourTripType.equalsIgnoreCase("RoundTrip")){
            if (UDropDate.equalsIgnoreCase("Select Drop Date")){
                Toast.makeText(activity, "Select Drop Date", Toast.LENGTH_SHORT).show();
                return;
            }

        }else{
            UDropDate="";
        }

        if (UPickLocation.equalsIgnoreCase(UDropLocation)){
            Toast.makeText(activity, "Select A Different Location", Toast.LENGTH_SHORT).show();
            return;
        }

        /*if (YourTripType.equalsIgnoreCase("RoundTrip")){
            if (!firstCiteyNAme.equalsIgnoreCase("") || multipleCityList.isEmpty()) {
               // Intent intent=new Intent(getApplicationContext(),SelectCar.class);
                Intent intent=new Intent(getApplicationContext(), BookingSummaryAndInvoiceActivity.class);
                intent.putExtra(CommonData.PickUpLocation,UPickLocation);
                if (YourTripType.equalsIgnoreCase("RoundTrip")){
                    intent.putExtra(CommonData.DropLocation,multipleCityList.get(0));
                }else{
                    intent.putExtra(CommonData.DropLocation,UDropLocation);
                }

                intent.putExtra(CommonData.TripType, TripFor);
                intent.putExtra(CommonData.PickupDate,UPickUpDate);
                if (YourTripType.equalsIgnoreCase("RoundTrip")){
                    intent.putExtra(CommonData.DropDate,UDropDate);
                    intent.putStringArrayListExtra(CommonData.SelectMultipleCity,multipleCityList);

                    Log.e("djkhgc",multipleCityList+"");

                }
                intent.putExtra(CommonData.PickUpTime,UPickUpTime);
                intent.putExtra(CommonData.TotalDistance,"");
                startActivity(intent);


            }else{
                Toast.makeText(activity, "Enter Pick &amp; Drop Location", Toast.LENGTH_SHORT).show();
                return;
            }
        }else {
            if (!firstCiteyNAme.equalsIgnoreCase("") || !SecondCiteyNAme.equalsIgnoreCase("")) {
                GetDistanceforTwoCity();
                Log.e("kdfljklg",  "Qasim0");
                Log.e("kdfljklg",  firstCiteyNAme+"");
                Log.e("kdfljklg",  SecondCiteyNAme+"");
            }else{
                Toast.makeText(activity, "City Name Not Found", Toast.LENGTH_SHORT).show();

            }
        }*/
        if (!firstCiteyNAme.equalsIgnoreCase("") || !SecondCiteyNAme.equalsIgnoreCase("")) {
            GetDistanceforTwoCity();
            Log.e("kdfljklg",  "Qasim0");
            Log.e("kdfljklg",  firstCiteyNAme+"");
            Log.e("kdfljklg",  SecondCiteyNAme+"");
        }else{
            Toast.makeText(activity, "City Name Not Found", Toast.LENGTH_SHORT).show();

        }

    }


    private void GetDistanceforTwoCity() {
        //String GetDistanceForTwoCity = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + firstCiteyNAme + "&destinations=" + SecondCiteyNAme + "&key=AIzaSyDyNGdERaDY8nlApHPWpYhSA9kXBqDLMQE";
        String GetDistanceForTwoCity = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + firstCiteyNAme + "&destinations=" + SecondCiteyNAme + "&key=AIzaSyC76Zr-75WbdmPZHnFmf0aRPgootI3McOo";
        Log.e("jnvkfkllkj",  "Qasim");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GetDistanceForTwoCity, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Status = jsonObject.getString("status");
                    if (Status.equalsIgnoreCase("OK")) {
                        Log.e("jnvkfkllkj",  "Qasim1");
                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                JSONArray jsonArray1 = jsonObject1.getJSONArray("elements");

                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    JSONObject jsonObjectElement = jsonArray1.getJSONObject(j);

                                    JSONObject jsonObjectDistance = jsonObjectElement.getJSONObject("distance");


                                    String Time = jsonObjectDistance.getString("text");
                                    String Distance = jsonObjectDistance.getString("value");
                                    int DistanceKilometer = Integer.parseInt(Distance);
                                    //Distance Calculate For kilometer
                                    NewDistance = String.valueOf(DistanceKilometer / 1000);

                                    Log.e("jnvkfkllkj", Time + "");
                                    Log.e("jnvkfkllkj", Distance + "");
                                    Log.e("jnvkfkllkj", NewDistance + "");




                                    Intent intent=new Intent(getApplicationContext(),BookingSummaryAndInvoiceActivity.class);
                                    intent.putExtra(CommonData.PickUpLocation,UPickLocation);
                                    /*if (YourTripType.equalsIgnoreCase("RoundTrip")){
                                        intent.putExtra(CommonData.DropLocation,"");
                                    }else{
                                        intent.putExtra(CommonData.DropLocation,UDropLocation);
                                    }*/
                                    intent.putExtra(CommonData.DropLocation,UDropLocation);
                                    intent.putExtra(CommonData.TripType, TripType);
                                    intent.putExtra(CommonData.TripFor, TripFor);
                                    intent.putExtra(CommonData.PickupDate,UPickUpDate);
                                    if (YourTripType.equalsIgnoreCase("RoundTrip")){
                                        intent.putExtra(CommonData.DropDate,UDropDate);
                                    }else{
                                        intent.putExtra(CommonData.DropDate,"");
                                        intent.putExtra(CommonData.SelectMultipleCity,"");

                                    }
                                    intent.putExtra(CommonData.PickUpTime,UPickUpTime);
                                    intent.putExtra(CommonData.TotalDistance,NewDistance);
                                    intent.putExtra(CommonData.CarType,UCarType);
                                    intent.putExtra(CommonData.CarModel,UCarModel);
                                    intent.putExtra(CommonData.SelectPackageId,PACKAGEID);
                                    startActivity(intent);

                                    Log.e("ldkfjljf",NewDistance+"");


                                }
                            }
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }
    private class GeocoderHandlerfrom extends Handler {
        @Override
        public void handleMessage(Message message) {
            try {
                String locationAddress;
                switch (message.what) {
                    case 1:
                        Bundle bundle = message.getData();
                        locationAddress = bundle.getString("address");
                        break;
                    default:
                        locationAddress = null;
                }
                Log.d("logi", locationAddress);

                String[] address = locationAddress.split(",");
                if (address.length > 3) {


                    latitude = Double.parseDouble(address[4]);
                    longitude = Double.parseDouble(address[5]);
                    if (tv_userCurrentLocation.getText().toString().length() > 6) {
                        //  focusCurrent(new LatLng(latitude, longitude));
                    }


                    // Log.d("print",address[i]);
                }
                //  oaddress.setText(ocity.getText().toString()+" ,"+ostate.getText().toString()+" ,"+ocountry.getText().toString());


            } catch (Exception e) {
                Log.d("FromCity", e.toString());
                e.printStackTrace();
            }
        }
    }



    List<Prediction> predictions = new ArrayList<>();

    private void loadData() {

        try {
            PlacesAutoCompleteAdapter placesAutoCompleteAdapter = new PlacesAutoCompleteAdapter(getApplicationContext(), predictions);

            // search_location.setThreshold(1);
            tv_userCurrentLocation.requestFocus();
            tv_userCurrentLocation.setThreshold(3);
            tv_userCurrentLocation.setAdapter(placesAutoCompleteAdapter);
            tv_userCurrentLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("jjasfkkajsg", new Gson().toJson(predictions.get(i)));
                    try {

                        firstCiteyNAme = tv_userCurrentLocation.getText().toString();



                        hideKeyboard();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            Log.d("LoadDataFrom", e.toString());
        }



    }
    private void loadDropData() {

        try {
            PlacesAutoCompleteAdapter placesAutoCompleteAdapter = new PlacesAutoCompleteAdapter(getApplicationContext(), predictions);

            // search_location.setThreshold(1);
            tv_dropLocation.requestFocus();
            tv_dropLocation.setThreshold(3);
            tv_dropLocation.setAdapter(placesAutoCompleteAdapter);
            tv_dropLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("jjasfkkajsg", new Gson().toJson(predictions.get(i)));
                    try {
                        hideKeyboardDrop();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            Log.d("LoadDataFrom", e.toString());
        }


    }

    private void hideKeyboard() {

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tv_userCurrentLocation.getWindowToken(), 0);
    }
    private void hideKeyboardDrop() {

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tv_dropLocation.getWindowToken(), 0);

    }

    private void LoadRoundTripDropListData() {

        try {
            PlacesAutoCompleteAdapter placesAutoCompleteAdapter = new PlacesAutoCompleteAdapter(getApplicationContext(), predictions);

            // search_location.setThreshold(1);
            tv_dropLocation.requestFocus();
            tv_dropLocation.setThreshold(3);
            tv_dropLocation.setAdapter(placesAutoCompleteAdapter);
            tv_dropLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("jjasfkkajsg", new Gson().toJson(predictions.get(i)));
                    try {

                        String DdropLocation=tv_dropLocation.getText().toString();

                        tv_dropLocation.setText("");


                      /*  multipleCityList.add(DdropLocation);



                        adapterSelectMultipleCity=new AdapterSelectMultipleCity(multipleCityList, getApplicationContext());
                        rv_SelectMultiCity.setAdapter(adapterSelectMultipleCity);
                        adapterSelectMultipleCity.notifyDataSetChanged();

                        if (multipleCityList.isEmpty()){
                            iv_crossdrop.setVisibility(View.GONE);
                        }else{
                            // iv_crossdrop.setVisibility(View.VISIBLE);
                            rv_SelectMultiCity.setVisibility(View.VISIBLE);

                        }*/


                        Log.e("fjhkdkdjkd",multipleCityList+"");
                        hideKeyboardDrop();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });





        } catch (Exception e) {
            Log.d("LoadDataFrom", e.toString());
        }



    }

    private void LoadPackageList() {

        showLoading(HomeActivity.this, "Please Wait");
        packageListData.clear();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("trip_type", TripType);
        hashMap.put("trip_for", TripFor);

        //Log.e("params", hashMap + "");
       // Log.e("kjkhj", TripType + "");
       // Log.e("kjkhj", TripFor + "");

///

        disposable.add(GlobalClassApiCall.initRetrofit().GetPackageList(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((user, throwatble) -> {
                            hideLoading();
                            if (user != null) {

                                Log.e("jksdhkfhk", "Response size: " + new Gson().toJson(user) + "");


                                if (user.getStatus().equals("200")) {

                                     Toast.makeText(activity, user.getMessage() + "", Toast.LENGTH_LONG).show();


                                    for (int i=0;i<user.getData().size();i++){

                                        packageListData.add(user.getData().get(i));

                                    }

                                    rv_selectPackage.setVisibility(View.VISIBLE);


                                    adapterSelectPackage=new AdapterSelectPackage(packageListData,getApplicationContext(),HomeActivity.this);
                                    rv_selectPackage.setAdapter(adapterSelectPackage);
                                    adapterSelectPackage.notifyDataSetChanged();


                                } else {
                                    // Toast.makeText(activity, user.getMessage() + "", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            }

                        }
                )
        );



    }


    @Override
    public void SelectPackageData(String PackageId, String PackageName) {
        PACKAGEID=PackageId;

        PACKAGENAME=PackageName;

        Log.e("jhgfd",PACKAGEID+"");

        tv_SelectPackage.setText(PACKAGENAME);

        rv_selectPackage.setVisibility(View.GONE);

    }



}
