package com.codediffusion.tyideuser.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class modelCarListData {
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
        private List<City> cityList = null;

        public List<City> getCityList() {
            return cityList;
        }

        public void setCityList(List<City> cityList) {
            this.cityList = cityList;
        }

    }

    public class City {

        @SerializedName("car_image")
        @Expose
        private String carImage;
        @SerializedName("car_name")
        @Expose
        private String carName;
        @SerializedName("shor_description")
        @Expose
        private String shorDescription;
        @SerializedName("total_km")
        @Expose
        private Integer totalKm;
        @SerializedName("per_kilo_price")
        @Expose
        private String perKiloPrice;
        @SerializedName("fixed_price")
        @Expose
        private String fixedPrice;
        @SerializedName("total_days")
        @Expose
        private Integer totalDays;
        @SerializedName("trip_type")
        @Expose
        private String tripType;
        @SerializedName("driver_all")
        @Expose
        private String driverAll;
        @SerializedName("booking_per")
        @Expose
        private Integer bookingPer;
        @SerializedName("from_place")
        @Expose
        private String fromPlace;
        @SerializedName("to_place")
        @Expose
        private String toPlace;
        @SerializedName("return_date")
        @Expose
        private String returnDate;
        @SerializedName("pick_up_date")
        @Expose
        private String pickUpDate;
        @SerializedName("pick_up_at_date")
        @Expose
        private String pickUpAtDate;

        public String getCarImage() {
            return carImage;
        }

        public void setCarImage(String carImage) {
            this.carImage = carImage;
        }

        public String getCarName() {
            return carName;
        }

        public void setCarName(String carName) {
            this.carName = carName;
        }

        public String getShorDescription() {
            return shorDescription;
        }

        public void setShorDescription(String shorDescription) {
            this.shorDescription = shorDescription;
        }

        public Integer getTotalKm() {
            return totalKm;
        }

        public void setTotalKm(Integer totalKm) {
            this.totalKm = totalKm;
        }

        public String getPerKiloPrice() {
            return perKiloPrice;
        }

        public void setPerKiloPrice(String perKiloPrice) {
            this.perKiloPrice = perKiloPrice;
        }

        public String getFixedPrice() {
            return fixedPrice;
        }

        public void setFixedPrice(String fixedPrice) {
            this.fixedPrice = fixedPrice;
        }

        public Integer getTotalDays() {
            return totalDays;
        }

        public void setTotalDays(Integer totalDays) {
            this.totalDays = totalDays;
        }

        public String getTripType() {
            return tripType;
        }

        public void setTripType(String tripType) {
            this.tripType = tripType;
        }

        public String getDriverAll() {
            return driverAll;
        }

        public void setDriverAll(String driverAll) {
            this.driverAll = driverAll;
        }

        public Integer getBookingPer() {
            return bookingPer;
        }

        public void setBookingPer(Integer bookingPer) {
            this.bookingPer = bookingPer;
        }

        public String getFromPlace() {
            return fromPlace;
        }

        public void setFromPlace(String fromPlace) {
            this.fromPlace = fromPlace;
        }

        public String getToPlace() {
            return toPlace;
        }

        public void setToPlace(String toPlace) {
            this.toPlace = toPlace;
        }

        public String getReturnDate() {
            return returnDate;
        }

        public void setReturnDate(String returnDate) {
            this.returnDate = returnDate;
        }

        public String getPickUpDate() {
            return pickUpDate;
        }

        public void setPickUpDate(String pickUpDate) {
            this.pickUpDate = pickUpDate;
        }

        public String getPickUpAtDate() {
            return pickUpAtDate;
        }

        public void setPickUpAtDate(String pickUpAtDate) {
            this.pickUpAtDate = pickUpAtDate;
        }

    }}
