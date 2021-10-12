package com.codediffusion.tyideuser.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class modelToAndFromCityDataApi {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public class Data {

        @SerializedName("city_list")
        @Expose
        private List<CityList> cityList = null;

        public List<CityList> getCityList() {
            return cityList;
        }

        public void setCityList(List<CityList> cityList) {
            this.cityList = cityList;
        }

    }
    public class CityList {

        @SerializedName("city_name")
        @Expose
        private String cityName;
        @SerializedName("city_id")
        @Expose
        private String cityId;

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

    }

}



