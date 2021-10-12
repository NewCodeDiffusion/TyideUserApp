package com.codediffusion.tyideuser.ApiCalingRetrofit;

import com.codediffusion.tyideuser.model.modelAddWalletAmount;
import com.codediffusion.tyideuser.model.modelCarListData;
import com.codediffusion.tyideuser.model.modelChangePassword;
import com.codediffusion.tyideuser.model.modelCheckout;
import com.codediffusion.tyideuser.model.modelCompleteOrder;
import com.codediffusion.tyideuser.model.modelCreateBooking;
import com.codediffusion.tyideuser.model.modelGetProfileData;
import com.codediffusion.tyideuser.model.modelRegisterData;
import com.codediffusion.tyideuser.model.modelRegisterMobileNo;
import com.codediffusion.tyideuser.model.modelRegisteredOtp;
import com.codediffusion.tyideuser.model.modelSelectPackage;
import com.codediffusion.tyideuser.model.modelToAndFromCityDataApi;
import com.codediffusion.tyideuser.model.modelUserBookingData;
import com.codediffusion.tyideuser.model.modelUserLogin;
import com.codediffusion.tyideuser.model.modelUserWalletAmount;
import com.codediffusion.tyideuser.model.modelWalletHistory;

import java.util.HashMap;

import io.reactivex.Single;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("user-register.php")
    Single<modelRegisterData> RegisterOtpData(@FieldMap HashMap<String, Object> hashMap
            /*@Header("unique") String apiKey*/);

    @FormUrlEncoded
    @POST("user-register.php")
    Single<modelRegisteredOtp> RegisterUserData(@FieldMap HashMap<String, Object> hashMap);


    @FormUrlEncoded
    @POST("user-update-profile.php")
    Single<modelRegisterData> UserRegisterData(@FieldMap HashMap<String, Object> hashMap);


    @FormUrlEncoded
    @POST("user-login.php")
    Single<modelUserLogin> UserLoginData(@FieldMap HashMap<String, Object> hashMap);


    @FormUrlEncoded
    @POST("user-get-details.php")
    Single<modelGetProfileData> GetProfileData(@FieldMap HashMap<String, Object> hashMap);

    @FormUrlEncoded
    @POST("get-search-city.php")
    Single<modelToAndFromCityDataApi> GetSerchData(@FieldMap HashMap<String, Object> hashMap);


    @FormUrlEncoded
    @POST("search-car.php")
    Single<modelCarListData> GetCarListData(@FieldMap HashMap<String, Object> hashMap);




    @FormUrlEncoded
    @POST("create-booking.php")
    Single<modelCreateBooking> CreateBookingData(@FieldMap HashMap<String, Object> hashMap);

    @FormUrlEncoded
    @POST("get-user-booking.php")
    Single<modelUserBookingData> BookingListData(@FieldMap HashMap<String, Object> hashMap);



    @FormUrlEncoded
    @POST("forgot-password.php")
    Single<modelRegisterMobileNo> ForgotPassData(@FieldMap HashMap<String, Object> hashMap);

    @FormUrlEncoded
    @POST("update-password.php")
    Single<modelChangePassword> ChangePassData(@FieldMap HashMap<String, Object> hashMap);


    @FormUrlEncoded
    @POST("trip-package.php")
    Single<modelSelectPackage> GetPackageList(@FieldMap HashMap<String, Object> hashMap);


    @FormUrlEncoded
    @POST("booking-details.php")
    Single<modelCheckout> CheckOutData(@FieldMap HashMap<String, Object> hashMap);



    @FormUrlEncoded
    @POST("payment-success.php")
    Single<modelCompleteOrder> GetCompleteOrderData(@FieldMap HashMap<String, Object> hashMap);


    @FormUrlEncoded
    @POST("user-get-wallet-amount.php")
    Single<modelUserWalletAmount> GetWalletAmountData(@FieldMap HashMap<String, Object> hashMap);



    @FormUrlEncoded
    @POST("recharge-wallet.php")
    Single<modelAddWalletAmount> GetWalletRecharge(@FieldMap HashMap<String, Object> hashMap);


    @FormUrlEncoded
    @POST("user-wallet-history.php")
    Single<modelWalletHistory> GetWalletHistory(@FieldMap HashMap<String, Object> hashMap);




///// for Get Method

  /*  @GET(Api.CityListData_Api)
    Single<LocationModel> CircleAreaList();*/
}
