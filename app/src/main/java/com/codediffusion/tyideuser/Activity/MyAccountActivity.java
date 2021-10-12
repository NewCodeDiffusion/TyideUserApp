package com.codediffusion.tyideuser.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.codediffusion.tyideuser.ApiCalingRetrofit.GlobalClassApiCall;
import com.codediffusion.tyideuser.Extra.CommonData;
import com.codediffusion.tyideuser.Extra.VolleyMultipartRequest;
import com.codediffusion.tyideuser.R;
import com.codediffusion.tyideuser.databinding.ActivityMyAccountBinding;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.codediffusion.tyideuser.Extra.CommonData.UserData;
import static com.codediffusion.tyideuser.Extra.CommonData.hideLoading;
import static com.codediffusion.tyideuser.Extra.CommonData.showLoading;

public class MyAccountActivity extends AppCompatActivity {
    private ActivityMyAccountBinding binding;
    private MyAccountActivity activity;
    private Context context;
    private CompositeDisposable disposable = new CompositeDisposable();

    int PICK_IMAGE_GALLERY = 100;
    int PICK_IMAGE_CAMERA = 101;
    private Bitmap bitmap;

    private String UserID, UName, UMobile, UEmail, Ugender;


    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_account);

        sharedPreferences = getSharedPreferences(UserData, MODE_PRIVATE);

        if (sharedPreferences.contains(CommonData.UserID)) {
            UserID = sharedPreferences.getString(CommonData.UserID, "");
        }

        context = activity = this;

        initialize();

    }

    private void initialize() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.ivUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });


        binding.tvUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(MyAccountActivity.this, "Profile Update Successfully", Toast.LENGTH_LONG).show();

                VAlidation();

            }
        });


        binding.rbMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.rbMale.setChecked(true);
                binding.rbFemale.setChecked(false);
                Ugender = "1";
            }
        });

        binding.rbFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.rbFemale.setChecked(true);
                binding.rbMale.setChecked(false);
                Ugender = "2";
            }
        });

        getProfileData();

    }

    private void VAlidation() {

        UName = binding.etUserName.getText().toString();
        UEmail = binding.etUserEmail.getText().toString();
        UMobile = binding.tvUserMobile.getText().toString();


        if (TextUtils.isEmpty(UName)) {
            binding.etUserName.setError("Enter User Name");
            binding.etUserName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(UEmail)) {
            binding.etUserEmail.setError("Enter Email ID");
            binding.etUserEmail.requestFocus();
            return;
        }

        if (Ugender.isEmpty()) {
            Toast.makeText(activity, "Select Gender Type", Toast.LENGTH_LONG).show();

        }


        if (bitmap == null) {
            Toast.makeText(activity, "Select Profile Image", Toast.LENGTH_SHORT).show();
            return;
        }

        UploadProfileDataApi();
    }


    public boolean checkPermission() {

        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : CommonData.Storage_Permision) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((activity),
                    listPermissionsNeeded.toArray(new
                            String[listPermissionsNeeded.size()]), 100);
            return false;
        } else {
            ChooseImageDialog();
        }
        return true;
    }

    private void ChooseImageDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(activity);
        builderSingle.setTitle("Choose Image");
        final ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(activity,
                        android.R.layout.simple_list_item_1);
        arrayAdapter.add("Camera");
        arrayAdapter.add("Gallery");

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                dialog.dismiss();
                try {
                    if (strName.equals("Gallery")) {


                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_GALLERY);
                    } else {

                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, PICK_IMAGE_CAMERA);

                    }
                } catch (SecurityException e) {
                    Toast.makeText(context, "Please allow permission to get image.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builderSingle.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                ChooseImageDialog();
            } else {

                ChooseImageDialog();
            }
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_GALLERY &&
                resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            Log.e("pathhhh", filePath.getPath() + "");
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                binding.userProfileImage.setImageBitmap(bitmap);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {
            try {

                bitmap = (Bitmap) data.getExtras().get("data");

                binding.userProfileImage.setImageBitmap(bitmap);


                Log.e("Image Path", "onActivityResult: ");
            } catch (Exception e) {

                e.printStackTrace();
            }
        }


    }

    private void getProfileData() {
        showLoading(context, "Please Wait");
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

                                    Toast.makeText(activity, user.getMessage() + "", Toast.LENGTH_LONG).show();

                                    binding.etUserEmail.setText(user.getData().getEmail());
                                    binding.tvUserMobile.setText(user.getData().getMobile());
                                    binding.etUserName.setText(user.getData().getName());

                                    if (user.getData().getPicture().equalsIgnoreCase("http://truecabs.in//upload/")) {
                                        //Glide.with(context).load(R.drawable.ic_man_1).into(binding.userProfileImage);
                                        RequestOptions requestOptions=new RequestOptions();
                                        requestOptions.placeholder(R.drawable.ic_man_1);
                                        requestOptions.error(R.drawable.ic_man_1);
                                        Glide.with(context).setDefaultRequestOptions(requestOptions).load(R.drawable.ic_man_1).
                                                diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.userProfileImage);
                                    } else {
                                        Glide.with(context).load(user.getData().getPicture()).into(binding.userProfileImage);

                                    }


                                    if (user.getData().getGender().equals("1")){
                                        binding.rbMale.setChecked(true);
                                        Ugender="1";
                                    }else{
                                        binding.rbFemale.setChecked(true);
                                        Ugender="2";
                                    }

                                } else {
                                    Toast.makeText(activity, user.getMessage() + "", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            }

                        }
                )
        );

    }

    private void UploadProfileDataApi() {
        showLoading(context, "Please Wait");
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, CommonData.UpdateUserData, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.e("upload_response", response + "");
                hideLoading();
                try {
                    JSONObject jsonObject = new JSONObject(new String(response.data));
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("1")) {

                        Toast.makeText(activity, message + "", Toast.LENGTH_SHORT).show();


                    } else {

                        Toast.makeText(activity, message + "", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", UserID);
                params.put("user_name", UName);
                params.put("user_mobile", UMobile);
                params.put("user_email", UEmail);
                params.put("gender", Ugender);
                Log.e("upload_params", params + "");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();

                params.put("picture", new DataPart(System.currentTimeMillis() + ".png", getFileDataFromDrawable(bitmap)));
                Log.e("PARAMS", params + "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        volleyMultipartRequest.setShouldCache(true);
        requestQueue.add(volleyMultipartRequest);

    }

    private byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


}